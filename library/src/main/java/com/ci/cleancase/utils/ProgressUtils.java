package com.ci.cleancase.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TextView;

import com.ci.cleancase.R;

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
        if(this.activity != null && this.activity.isFinishing()){
            dlgProgress = new ProgressDialog(activity);
            this.activity = activity;
        }
        if (!dlgProgress.isShowing()) {
            dlgProgress.show();
            dlgProgress.setCancelable(false);
            dlgProgress.setContentView(R.layout.view_centered_progress_indicator);
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
}
