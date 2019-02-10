package com.zerofiltre.snapanonym.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zerofiltre.snapanonym.R;
import com.zerofiltre.snapanonym.activity.Snap.SnapsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void onExploreSnaps(View view) {
        Intent intent = new Intent(this, SnapsActivity.class);
        startActivity(intent);

    }
}
