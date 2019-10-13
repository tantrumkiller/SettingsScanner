package com.fourthlap.settingsscanner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.fourthlap.settingsscanner.alarm.AlarmScheduler;
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.setting.Setting;
import com.fourthlap.settingsscanner.setting.SettingsConfig;
import com.fourthlap.settingsscanner.userpreference.UserPreferencesStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionsRequiredActivity extends AppCompatActivity {

  private final UserPreferencesStore userPreferencesStore;
  private final SettingsConfig settingConfiguration;
  private final AlarmScheduler alarmScheduler;
  private final ReminderNotificationHandler reminderNotificationHandler;

  private RecyclerView recyclerView;
  private ActionRequiredRecyclerViewAdapter recycleViewAdapter;

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
    setupRecycleView();
    setupPullToRefresh();
    setupFloatingButton();
    populateActionsListAndClearNotification();
    alarmScheduler.scheduleAlarm(getApplicationContext());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.toolbar_settings_menu, menu);
    return true;
  }

  @Override
  public void onResume() {
    super.onResume();
    populateActionsListAndClearNotification();
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
        } else if (arg0.getItemId() == R.id.goto_rate_us) {
          Intent rateIntent = new Intent(Intent.ACTION_VIEW,
              Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
          try {
            startActivity(rateIntent);
          } catch (Exception e) {
            Log.i("ActionsRequired", "Failed to open play store" + e);
          }
        }

        return false;
      }
    });
  }

  private void setupRecycleView() {
    this.recyclerView = findViewById(R.id.actions_required_recycler_view);
    this.recycleViewAdapter = new ActionRequiredRecyclerViewAdapter(getApplicationContext(),
        new ArrayList<Setting>());

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(true);

    // use a linear layout manager
    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    recyclerView.setAdapter(recycleViewAdapter);
    recyclerView.addItemDecoration(
        new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
  }

  private void setupPullToRefresh() {
    final SwipeRefreshLayout pullToRefresh = findViewById(R.id.actions_required_swipe_refresh);
    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        populateActionsListAndClearNotification(); // your code
        pullToRefresh.setRefreshing(false);
      }
    });
  }

  private void setupFloatingButton() {
    FloatingActionButton fab = findViewById(R.id.floatingActionButton);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate));
        populateActionsListAndClearNotification();
      }
    });
  }

  private void populateActionsListAndClearNotification() {
    final List<Setting> settingsList = new ArrayList<>();
    final Context context = getApplicationContext();
    final SharedPreferences sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context);
    final Set<Setting> settings = userPreferencesStore
        .getSettingsEnabledForWatch(sharedPreferences);
    for (final Setting setting : settings) {
      if (settingConfiguration.getHandler(setting).isEnabled(context)) {
        settingsList.add(setting);
      }
    }

    recycleViewAdapter.setData(settingsList);
    recycleViewAdapter.notifyDataSetChanged();

    if (settingsList.size() == 0) {
      NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      reminderNotificationHandler.cancelNotification(nm);
    }
  }
}
