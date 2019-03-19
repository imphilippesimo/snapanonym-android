package com.zerofiltre.snapanonym.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.infrastructure.Network.AppUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.GPSUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.NetworkUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.Receiver.ConnectionReceiver;
import com.zerofiltre.snapanonym.view.activity.Snap.SnapsActivity;

import java.io.File;
import java.io.IOException;

import static com.zerofiltre.snapanonym.infrastructure.Network.AppUtils.createImageFile;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ConnectivityManager mConnectivityManager;
    private boolean isGPS;
    private boolean isConnected;
    private boolean wasLost = false;
    private Snackbar mSnackbar;
    private View mCurrentView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mCurrentView = getWindow().getDecorView().findViewById(android.R.id.content);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //TODO  background colors for snacbars depending on the message
        mConnectivityManager.requestNetwork(builder.build(), new ConnectionReceiver(new ConnectionReceiver.onConnectionListener() {
            @Override
            public void showConnectionAvailable() {
                //if (wasLost) {
                if (mSnackbar != null)
                    mSnackbar.dismiss();
                mSnackbar = Snackbar.make(mCurrentView, R.string.backed_online_joy, Snackbar.LENGTH_SHORT);
                mSnackbar.show();
                //}

            }

            @Override
            public void showConnectionUnavailable() {
                if (mSnackbar != null)
                    mSnackbar.dismiss();
                wasLost = true;
                mSnackbar = Snackbar.make(mCurrentView, R.string.offline_get_back_online, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.dismiss), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSnackbar.dismiss();
                            }
                        }).setActionTextColor(getResources().getColor(R.color.colorAccent));
                mSnackbar.show();
            }
        }), 2000);


        new GPSUtils(this).turnGPSOn(new GPSUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        new NetworkUtils(this).getNetworkStatus(new NetworkUtils.OnNetworkListener() {
            @Override
            public void networkStatus(boolean networkEnabled) {
                isConnected = networkEnabled;
            }
        });
    }

    public void onExploreSnaps(View view) {
        if (isGPS && isConnected) {
            Intent intent = new Intent(this, SnapsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.enable_required_settings), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppUtils.GPS_REQUEST) {
                isGPS = true;
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void onPublishSnap(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}
