package com.fourthlap.settingsscanner.viewelements;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fourthlap.settingsscanner.R;
import com.fourthlap.settingsscanner.setting.Setting;
import com.fourthlap.settingsscanner.setting.SettingsConfiguration;
import java.util.ArrayList;
import java.util.List;

public class ActionRequiredRecyclerViewAdapter extends
    RecyclerView.Adapter<ActionRequiredRecyclerViewAdapter.ViewHolder> {

  private final LayoutInflater layoutInflater;
  private final Drawable arrowImage;
  private final SettingsConfiguration settingsConfiguration;

  private List<Setting> enabledSettings;

  public ActionRequiredRecyclerViewAdapter(final Context context, final SettingsConfiguration settingsConfiguration) {
    this.layoutInflater = LayoutInflater.from(context);
    this.settingsConfiguration = settingsConfiguration;
    this.enabledSettings = new ArrayList<>();
    this.arrowImage = context.getResources()
        .getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp);
  }

  // inflates the row layout from xml when needed
  @Override
  public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    final View view = layoutInflater.inflate(R.layout.action, parent, false);
    return new ViewHolder(view);
  }

  // binds the data to the TextView in each row
  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    int actionName = settingsConfiguration.getDisplayableNameResId(enabledSettings.get(position));
    holder.textView.setText(actionName);
    holder.imageView.setImageDrawable(arrowImage);
  }

  @Override
  public int getItemCount() {
    return enabledSettings.size() ;
  }

  public void setData(final List<Setting> settings) {
    this.enabledSettings = settings;
  }

  // stores and recycles views as they are scrolled off screen
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textView;
    private final ImageView imageView;

    ViewHolder(final View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.action);
      itemView.setOnClickListener(this);

      imageView = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void onClick(final View view) {
      Log.i("ActionRequiredRecycler", enabledSettings.get(getAdapterPosition()) + " is clicked");
      settingsConfiguration.getHandler(enabledSettings.get(getAdapterPosition()))
          .openSettingsMenu(view.getContext());
    }
  }
}