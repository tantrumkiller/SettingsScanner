package com.fourthlap.settingsscanner;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
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
import com.fourthlap.settingsscanner.notification.ReminderNotificationHandler;
import com.fourthlap.settingsscanner.scheduler.ScanScheduler;
import com.fourthlap.settingsscanner.setting.Setting;
import com.fourthlap.settingsscanner.setting.SettingsConfiguration;
import com.fourthlap.settingsscanner.setting.SettingsScanner;
import com.fourthlap.settingsscanner.userpreference.UserPreferences;
import com.fourthlap.settingsscanner.viewelements.ActionRequiredRecyclerViewAdapter;
import com.fourthlap.settingsscanner.viewelements.GotoPreferenceButton;
import java.util.Calendar;
import java.util.List;

public class ActionsRequiredActivity extends AppCompatActivity {

  private final SettingsScanner settingsScanner;
  private final ScanScheduler scanScheduler;
  private final ReminderNotificationHandler reminderNotificationHandler;
  private final SettingsConfiguration settingsConfiguration;
  private final UserPreferences userPreferences;
  private RecyclerView recyclerView;
  private ActionRequiredRecyclerViewAdapter recycleViewAdapter;

  public ActionsRequiredActivity() {
    super();
    this.userPreferences = new UserPreferences();
    this.settingsConfiguration = new SettingsConfiguration();
    this.settingsScanner = new SettingsScanner(userPreferences, settingsConfiguration);
    this.scanScheduler = new ScanScheduler(userPreferences);
    this.reminderNotificationHandler = new ReminderNotificationHandler();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.actions_required_page);
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    executeOnCreate();
  }

  void executeOnCreate(){
    setupToolbar();
    setupRecycleView();
    setupPullToRefresh();
    setupFloatingButton();
    populateActionsListAndClearNotification();

    scanScheduler.scheduleNextScan(getApplicationContext(), Calendar.getInstance());

    findViewById(R.id.main_goto_preferences_button).setOnClickListener(new GotoPreferenceButton());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    final MenuInflater inflater = getMenuInflater();
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
          startActivity(new Intent(getApplicationContext(), UserPreferencesActivity.class));
        } else if (arg0.getItemId() == R.id.goto_rate_us) {
          takeUserToRateUs();
        }

        return false;
      }
    });
  }

  private void setupRecycleView() {
    this.recyclerView = findViewById(R.id.actions_required_recycler_view);
    this.recycleViewAdapter = new ActionRequiredRecyclerViewAdapter(getApplicationContext(),
        settingsConfiguration);

     recyclerView.setHasFixedSize(false);

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
        populateActionsListAndClearNotification();
        pullToRefresh.setRefreshing(false);
      }
    });
  }

  private void setupFloatingButton() {
    final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate));
        populateActionsListAndClearNotification();
      }
    });
  }

  private void populateActionsListAndClearNotification() {
    setVisibilityOfActionsRequiredSection(View.VISIBLE);
    findViewById(R.id.no_actions_needed).setVisibility(View.GONE);

    final List<Setting> settingsList = settingsScanner.getEnabledSettings(getApplicationContext());
    recycleViewAdapter.setData(settingsList);
    recycleViewAdapter.notifyDataSetChanged();

    if (settingsList.size() == 0) {
      final NotificationManager notificationManager = (NotificationManager) getSystemService(
          Context.NOTIFICATION_SERVICE);
      reminderNotificationHandler.cancelNotification(notificationManager);
      setVisibilityOfActionsRequiredSection(View.GONE);
      findViewById(R.id.no_actions_needed).setVisibility(View.VISIBLE);
    }
  }

  private void setVisibilityOfActionsRequiredSection(int visibility) {
    findViewById(R.id.actions_required_heading).setVisibility(visibility);
    findViewById(R.id.actions_required_sub_heading).setVisibility(visibility);
    findViewById(R.id.actions_required_swipe_refresh).setVisibility(visibility);
  }

  private void takeUserToRateUs() {
    final Intent rateIntent = new Intent(Intent.ACTION_VIEW,
        Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
    try {
      startActivity(rateIntent);
    } catch (Exception e) {
      Log.i("ActionsRequired", "Failed to open play store" + e);
    }
  }
}
