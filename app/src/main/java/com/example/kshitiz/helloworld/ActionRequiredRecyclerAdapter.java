package com.example.kshitiz.helloworld;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ActionRequiredRecyclerAdapter extends RecyclerView.Adapter<ActionRequiredRecyclerAdapter.ViewHolder> {

    private final List<String> mData;
    private final LayoutInflater mInflater;
    private final ItemClickListener mClickListener;

    // data is passed into the constructor
    ActionRequiredRecyclerAdapter(final Context context,final  List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mClickListener = null;
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
        final String action = mData.get(position);
        holder.myTextView.setText(action);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
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
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            Log.i("Recycle", "Clicked");
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}