package com.zerofiltre.snapanonym.infrastructure.Network;

import android.util.Base64;

import com.zerofiltre.snapanonym.model.SimpleLocation;

public class AppUtils {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;

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
}
