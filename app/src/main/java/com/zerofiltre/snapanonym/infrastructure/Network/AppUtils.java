package com.zerofiltre.snapanonym.infrastructure.Network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Base64;

import com.zerofiltre.snapanonym.model.SimpleLocation;
import com.zerofiltre.snapanonym.view.activity.Snap.SnapsActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class AppUtils {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public static final String CURRENT_LOCATION = "CURRENT_LOCATION";
    public static final String FILE_PATH = "FILE_PATH";
    public static final String CURRENT_PHOTO_PATH_EXTRA = "CURRENT_PHOTO_PATH_EXTRA";

    public static double distanceBetweenAsMeters(SimpleLocation origin, SimpleLocation destination, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(destination.getLatitude() - origin.getLatitude());
        double lonDistance = Math.toRadians(destination.getLongitude() - origin.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(origin.getLongitude())) * Math.cos(Math.toRadians(destination.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static byte[] bytesFromStringContent(String contentAsString) {

        return Base64.decode(contentAsString, Base64.DEFAULT);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }


    public static void requestPermissions(Activity activity, String[] permissions, int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity,
                permissions,
                permissionRequestCode);

    }
}
