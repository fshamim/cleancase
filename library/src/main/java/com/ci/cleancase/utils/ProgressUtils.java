package com.ci.cleancase.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.TextView;

import com.ci.cleancase.R;
import com.ci.cleanlog.L;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by mmansar on 10.10.14.
 */
@Singleton
public class ProgressUtils {
    private ProgressDialog dlgProgress;
    private Activity activity;

    /**
     * Inject annotation is needed by Dagger
     */
    @Inject
    public ProgressUtils() {
    }

    /**
     * Indicate a progress bar on the given activity
     *
     * @param activity android context
     */
    public void showProgress(Activity activity, String loadingMsg) {
        if (dlgProgress == null) {
            this.activity = activity;
            dlgProgress = new ProgressDialog(activity);
        }
        if (this.activity != null && this.activity.isFinishing()) {
            dlgProgress = new ProgressDialog(activity);
            this.activity = activity;
        }
        if (!dlgProgress.isShowing()) {
            try {
                dlgProgress.show();
            } catch (WindowManager.BadTokenException ex) {
                L.e("", "Progress show error", ex);
            }
            dlgProgress.setCancelable(false);
            if(Build.VERSION.SDK_INT >= 21 ){
                View dialogView = activity.getLayoutInflater().inflate(R.layout.view_centered_progress_indicator, null);
                dlgProgress.setContentView(dialogView);
                clearParentsBackgrounds(dialogView);
            }else {
                dlgProgress.setContentView(R.layout.view_centered_progress_indicator);
            }
        }
        if (loadingMsg != null) {
            TextView tvProgressMsg = (TextView) dlgProgress.findViewById(R.id.tv_progress_msg);
            if (tvProgressMsg != null) {
                tvProgressMsg.setText(loadingMsg);
            }
        }
    }

    /**
     * hide the previously shown progress bar
     */
    public void hideProgress() {
        if (dlgProgress != null && dlgProgress.isShowing()) {
            dlgProgress.dismiss();
        }
    }

    /**
     * check if the progress bar is showing
     *
     * @return true if showing
     */
    public boolean isInProgress() {
        return dlgProgress != null && dlgProgress.isShowing();
    }

    /**
     * This function is used for clearing out the background post show method of the view.
     * Currently it is used for making the default white material background of the system widgets
     * like alert dialog to make it transparent for the purpose of not showing the material
     * paper dialog
     * @param view whose background should become transparent
     */
    public static void clearParentsBackgrounds(View view) {
        while (view != null) {
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                view = (View) parent;
                view.setBackgroundResource(android.graphics.Color.TRANSPARENT);
            } else {
                view = null;
            }
        }
    }
}
