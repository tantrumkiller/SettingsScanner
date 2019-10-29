package com.fourthlap.settingsscanner.viewelements;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.fourthlap.settingsscanner.UserPreferencesActivity;

public class GotoPreferenceButton implements OnClickListener {

  public void onClick(View v) {
    final Context context = v.getContext();
    context.startActivity(new Intent(context, UserPreferencesActivity.class));
  }
}
