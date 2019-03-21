package com.zerofiltre.snapanonym.view.activity.Snap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.infrastructure.Network.AppUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.Loader.GetSnapsLoader;
import com.zerofiltre.snapanonym.model.Snap;
import com.zerofiltre.snapanonym.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.zerofiltre.snapanonym.infrastructure.Network.AppUtils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class SnapsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Snap>> {

    //Member variables
    private RecyclerView mRecyclerView;
    private ArrayList<Snap> mSnapsData;
    private SnapsAdapter mAdapter;
    private RelativeLayout spinner;
    private static final double distance = 500;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean isGPS;
    private boolean isConnected;
    private LoaderManager.LoaderCallbacks<? extends Object> mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mRecyclerView = findViewById(R.id.snaps_recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSnapsData = new ArrayList<>();

        mAdapter = new SnapsAdapter(mSnapsData, this);
        mRecyclerView.setAdapter(mAdapter);
        spinner = findViewById(R.id.spinner);

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

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted, we can loadSnaps
                    retrieveSnaps();
                } else {
                    //Permission not granted, go back to the home page
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
                return;
            }
        }
    }


    @NonNull
    @Override
    public Loader<List<Snap>> onCreateLoader(int id, @Nullable Bundle bundle) {

        return new GetSnapsLoader(this, mCurrentLocation, distance);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Snap>> loader, List<Snap> data) {


        spinner.setVisibility(View.GONE);
        mSnapsData = (ArrayList<Snap>) data;
        mAdapter.setmSnapsData(mSnapsData);
        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Snap>> loader) {

    }

    private void loadSnaps() {


        spinner.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            if (!AppUtils.hasPermissions(this, permissions)) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    AppUtils.requestPermissions(this,permissions,PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    // No explanation needed; request the permission
                }
            } else {
                //Permission already granted , get the current location and loadSnaps
                retrieveSnaps();
            }
        }


    }

    @SuppressLint("MissingPermission")
    private void retrieveSnaps() {

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location result) {
                        // Got last known location. In some rare situations this can be null.
                        if (result != null) {
                            // Logic to handle location object
                            mCurrentLocation = result;
                            getSupportLoaderManager().restartLoader(0, null, mContext);
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppUtils.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }


}
