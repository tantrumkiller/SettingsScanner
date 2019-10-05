package com.example.kshitiz.helloworld;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kshitiz.helloworld.setting.Setting;
import com.example.kshitiz.helloworld.setting.SettingsConfig;

import java.util.List;

public class ActionRequiredRecyclerViewAdapter extends RecyclerView.Adapter<ActionRequiredRecyclerViewAdapter.ViewHolder> {

    private final List<Setting> mData;
    private final LayoutInflater mInflater;
    private final SettingsConfig  settingsConfig;

    ActionRequiredRecyclerViewAdapter(final Context context, final  List<Setting> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.settingsConfig = new SettingsConfig();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.action, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (isDataEmpty()) {
            holder.myTextView.setText(R.string.no_action_required_text);
            return;
        }

        final String action = mData.get(position).toString();
        holder.myTextView.setText(action);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return isDataEmpty() ? 1 : mData.size();
    }

    private boolean isDataEmpty() {
        return mData == null || mData.size() == 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.action);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Log.i("Recycle", "Clicked");
            if (isDataEmpty()){
                Log.i("Recycle", "Empty");
                return;
            }

            settingsConfig.getHandler(mData.get(getAdapterPosition())).openSettingsMenu(view.getContext());
        }
    }
}