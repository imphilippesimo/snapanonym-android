package com.zerofiltre.snapanonym.infrastructure.Network.Loader;

import android.content.Context;
import android.location.Location;

import com.zerofiltre.snapanonym.infrastructure.Network.AppUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.NetworkUtils;
import com.zerofiltre.snapanonym.model.SimpleLocation;
import com.zerofiltre.snapanonym.model.Snap;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


public class SnapLoader extends AsyncTaskLoader<List<Snap>> {

    private Double mScope;
    private Location mCurrentLocation;

    public SnapLoader(@NonNull Context context, Location currentLocation, Double mScope) {
        super(context);
        this.mScope = mScope;
        this.mCurrentLocation = currentLocation;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    @Nullable
    @Override
    public List<Snap> loadInBackground() {
        List<Snap> snaps = NetworkUtils.getSnaps(mCurrentLocation, mScope);
        for (Snap snap : snaps) {
            snap.setDistance((int) AppUtils.distanceBetweenAsMeters(
                    new SimpleLocation(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude()),
                    snap.getPostedAt(),
                    0,
                    0));
        }
        return snaps;
    }


}
