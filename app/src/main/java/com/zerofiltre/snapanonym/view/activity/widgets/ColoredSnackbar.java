package com.zerofiltre.snapanonym.view.activity.widgets;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.zerofiltre.snapanonym.R;

public class ColoredSnackbar {


    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }

        return snackbar;
    }

    public static Snackbar info(Snackbar snackbar, Context context) {
        return colorSnackBar(snackbar, context.getResources().getColor(R.color.blueInfo));
    }

    public static Snackbar warning(Snackbar snackbar, Context context) {
        return colorSnackBar(snackbar, context.getResources().getColor(R.color.colorAccent));
    }

    public static Snackbar alert(Snackbar snackbar, Context context) {
        return colorSnackBar(snackbar, context.getResources().getColor(R.color.redError));
    }

    public static Snackbar confirm(Snackbar snackbar, Context context) {
        return colorSnackBar(snackbar, context.getResources().getColor(R.color.greenConfirm));
    }
}
