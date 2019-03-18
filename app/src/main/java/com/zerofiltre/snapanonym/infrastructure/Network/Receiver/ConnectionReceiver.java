package com.zerofiltre.snapanonym.infrastructure.Network.Receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.zerofiltre.snapanonym.R;

@SuppressLint("NewApi")
public class ConnectionReceiver extends ConnectivityManager.NetworkCallback {


    Context mContext;
    private boolean wasLost = false;
    private onConnectionListener onConnectionListener;


    public ConnectionReceiver(ConnectionReceiver.onConnectionListener onConnectionListener) {
        super();
        this.onConnectionListener = onConnectionListener;
    }

    @Override
    public void onAvailable(Network network) {
        if (onConnectionListener != null)
            onConnectionListener.showConnectionAvailable();

    }


    @Override
    public void onLost(Network network) {
        if (onConnectionListener != null)
            onConnectionListener.showConnectionUnavailable();

    }

    @Override
    public void onUnavailable() {
        if (onConnectionListener != null)
            onConnectionListener.showConnectionUnavailable();
        
    }

    public interface onConnectionListener {
        public void showConnectionAvailable();

        public void showConnectionUnavailable();
    }


}
