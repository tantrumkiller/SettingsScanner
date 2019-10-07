package com.fourthlap.a.settingspatroller;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.fourthlap.a.settingspatroller.alarm.AlarmScheduler;
import com.fourthlap.a.settingspatroller.notification.ReminderNotificationHandler;
import com.fourthlap.a.settingspatroller.setting.Setting;
import com.fourthlap.a.settingspatroller.setting.SettingsConfig;
import com.fourthlap.a.settingspatroller.userpreference.UserPreferencesStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionsRequiredActivity extends AppCompatActivity {
    private final UserPreferencesStore userPreferencesStore;
    private final SettingsConfig settingConfiguration;
    private final AlarmScheduler alarmScheduler;
    private final ReminderNotificationHandler reminderNotificationHandler;

    public ActionsRequiredActivity() {
        super();
        this.userPreferencesStore = new UserPreferencesStore();
        this.settingConfiguration = new SettingsConfig();
        this.alarmScheduler = new AlarmScheduler();
        this.reminderNotificationHandler = new ReminderNotificationHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actions_required);

        setupToolbar();
        populateListAndClearNotification();
        setupPullToRefresh();
        alarmScheduler.scheduleAlarm(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate));
                populateListAndClearNotification();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_settings_menu, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        populateListAndClearNotification();
    }

    private void populateListAndClearNotification() {
        final RecyclerView recyclerView = findViewById(R.id.actions_required_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final List<Setting> lists = new ArrayList<>();
        final Context context = getApplicationContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final Set<Setting> settings = userPreferencesStore.getSettingsEnabledForWatch(sharedPreferences);
        for (final Setting setting : settings) {
            if (settingConfiguration.getHandler(setting).isEnabled(context)) {
                lists.add(setting);
            }
        }

        final RecyclerView.Adapter mAdapter = new ActionRequiredRecyclerViewAdapter(getApplicationContext(), lists);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        if(lists.size() == 0) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            reminderNotificationHandler.cancelNotification(nm);
        }
    }

    private void setupToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_actions_required);
        setSupportActionBar(myToolbar);

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if (arg0.getItemId() == R.id.goto_preferences_button) {
                    Intent intent = new Intent(getApplicationContext(), UserPreferencesActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private void setupPullToRefresh() {
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.actions_required_swipe_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateListAndClearNotification(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }
}