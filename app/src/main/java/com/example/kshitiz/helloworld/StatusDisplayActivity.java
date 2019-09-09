package com.example.kshitiz.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StatusDisplayActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Loop through whitelisted settings
    // For enabled settings display text area
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_display);
        recyclerView = (RecyclerView) findViewById(R.id.actions_requried_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        List<String> lists = new ArrayList<>();
        lists.add("myDataset1");
        lists.add("myDataset2");
        lists.add("myDataset3");
        lists.add("myDataset4");

        mAdapter = new MyAdapter(getApplicationContext(), lists);
        recyclerView.setAdapter(mAdapter);
    }
}
