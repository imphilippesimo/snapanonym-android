package com.zerofiltre.snapanonym.activity.Snap;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.data.Snap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class SnapsActivity extends AppCompatActivity {

    //Member variables
    private RecyclerView mRecyclerView;
    private ArrayList<Snap> mSnapsData;
    private SnapsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.snaps_recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSnapsData = new ArrayList<>();

        mAdapter = new SnapsAdapter(mSnapsData, this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder originalViewHolder, @NonNull RecyclerView.ViewHolder targetViewHolder) {
                int from = originalViewHolder.getAdapterPosition();
                int to = targetViewHolder.getAdapterPosition();
                Collections.swap(mSnapsData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mSnapsData.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        });

        helper.attachToRecyclerView(mRecyclerView);

        loadSnaps();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                loadSnaps();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //TODO Run this asynchronously
    private void loadSnaps() {

        //loading snaps manually || should get the snaps from network asynchronously
        String[] snapDistancesList = getResources().getStringArray(R.array.snaps_distance);
        TypedArray snapsImageResource = getResources().obtainTypedArray(R.array.snaps_images);

        //clear the existing data to avoid duplication: should we clear previous snaps
        mSnapsData.clear();

        for (int i = 0; i < snapDistancesList.length; i++) {
            mSnapsData.add(new Snap(i,snapDistancesList[i], snapsImageResource.getResourceId(i, 0)));
        }

        snapsImageResource.recycle();

        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();


    }

}
