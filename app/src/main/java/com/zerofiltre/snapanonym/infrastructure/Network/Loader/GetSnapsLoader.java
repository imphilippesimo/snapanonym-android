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


public class GetSnapsLoader extends AsyncTaskLoader<List<Snap>> {

    private Double mScope;
    private Location mCurrentLocation;

    public GetSnapsLoader(@NonNull Context context, Location currentLocation, Double mScope) {
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
            double distanceBetweenAsMeters = AppUtils.distanceBetweenAsMeters(
                    new SimpleLocation(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude()),
                    snap.getPostedAt(),
                    0,
                    0);

            snap.setDistance((int) distanceBetweenAsMeters);
        }
        return snaps;
    }


}
