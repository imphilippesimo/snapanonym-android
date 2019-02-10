package com.zerofiltre.snapanonym.activity.Snap;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.data.Comment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SnapDetailsActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private List<Comment> mComments;
    private SnapDetailsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_detail_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int imageResourceId = getIntent().getIntExtra(SnapsAdapter.EXTRA_SNAP_IMAGE_ID, 0);
        mRecyclerView = findViewById(R.id.snap_details_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mComments = new ArrayList<Comment>();
        mAdapter = new SnapDetailsAdapter(this, mComments, imageResourceId);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadSnapDetails();
    }

    //TODO Run this asynchronously
    private void loadSnapDetails() {

        for (int i = 0; i < 15; i++) {
            mComments.add(new Comment(i, getString(R.string.commentator_id_sample)+i, getString(R.string.comment_minified_sample)));
        }
        mAdapter.notifyDataSetChanged();

    }


}
