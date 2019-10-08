package com.fourthlap.a.settingswatcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthlap.a.settingswatcher.setting.Setting;
import com.fourthlap.a.settingswatcher.setting.SettingsConfig;

import java.util.List;

public class ActionRequiredRecyclerViewAdapter extends RecyclerView.Adapter<ActionRequiredRecyclerViewAdapter.ViewHolder> {

    private final List<Setting> mData;
    private final LayoutInflater mInflater;
    private final SettingsConfig  settingsConfig;
    private final Drawable arrowImage;

    ActionRequiredRecyclerViewAdapter(final Context context, final  List<Setting> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.settingsConfig = new SettingsConfig();
        this.arrowImage = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.action, parent, false);
        view.setBackgroundResource(R.color.actionColor);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isDataEmpty()) {
            holder.textView.setText(R.string.no_action_required_text);
            return;
        }

        final String action = mData.get(position).toString();
        holder.textView.setText(action);
        holder.textView.setBackgroundResource(R.color.actionColor);
        holder.imageView.setImageDrawable(arrowImage);
    }

    @Override
    public int getItemCount() {
        return isDataEmpty() ? 1 : mData.size();
    }

    private boolean isDataEmpty() {
        return mData == null || mData.size() == 0;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.action);
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(final View view) {
            if (isDataEmpty()){
                return;
            }

            settingsConfig.getHandler(mData.get(getAdapterPosition())).openSettingsMenu(view.getContext());
        }
    }
}