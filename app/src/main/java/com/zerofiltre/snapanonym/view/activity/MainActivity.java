package com.zerofiltre.snapanonym.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.infrastructure.Network.AppUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.GPSUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.NetworkUtils;
import com.zerofiltre.snapanonym.infrastructure.Network.Receiver.ConnectionReceiver;
import com.zerofiltre.snapanonym.view.activity.Snap.SnapPublisher;
import com.zerofiltre.snapanonym.view.activity.Snap.SnapsActivity;
import com.zerofiltre.snapanonym.view.activity.widgets.ColoredSnackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import static com.zerofiltre.snapanonym.infrastructure.Network.AppUtils.CURRENT_PHOTO_PATH_EXTRA;
import static com.zerofiltre.snapanonym.infrastructure.Network.AppUtils.hasPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int FILE_PERMISSIONS_REQUEST = 2534;

    ConnectivityManager mConnectivityManager;
    private boolean isGPS;
    private boolean isConnected;
    private boolean wasLost = false;
    private Snackbar mSnackbar;
    private View mCurrentView;
    File mPhotoFile = null;
    private ImageView mBackground;
    private Context mContext;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContext = this;
        mCurrentView = getWindow().getDecorView().findViewById(android.R.id.content);
        mBackground = findViewById(R.id.main_bg_image);
        mBackground.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
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
                ColoredSnackbar.confirm(mSnackbar, mContext).show();
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
                        }).setActionTextColor(getResources().getColor(R.color.white));
                ColoredSnackbar.warning(mSnackbar, mContext).show();
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
        //if (isGPS && isConnected) {
        Intent intent = new Intent(this, SnapsActivity.class);
        startActivity(intent);
        // } else {
        // Toast.makeText(this, getString(R.string.enable_required_settings), Toast.LENGTH_SHORT).show();
        // }


    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();

        }
        super.onBackPressed();
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
            setPic();
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onPublishSnap(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, FILE_PERMISSIONS_REQUEST);
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FILE_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted, we can loadSnaps
                    openCamera();
                } else {
                    //Permission not granted, go back to the home page
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                }
                return;
            }
        }
    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                mPhotoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (mPhotoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.zerofiltre.snapanonym.fileprovider",
                        mPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void setPic() {
        Intent intent = new Intent(this, SnapPublisher.class);
        intent.putExtra(CURRENT_PHOTO_PATH_EXTRA, mPhotoFile);
        startActivity(intent);
    }


}
