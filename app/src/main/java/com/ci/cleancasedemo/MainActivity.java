package com.ci.cleancasedemo;

import android.content.Context;
import android.os.Bundle;

import com.ci.cleancase.android.CleanActivity;
import com.ci.cleancase.utils.ProgressUtils;
import com.ci.cleanlog.L;
import com.ci.ibus.events.InProgressEvent;
import com.ci.ibus.events.ProgressFinishedEvent;
import com.ci.ibus.events.UnauthorizedErrorEvent;
import com.ci.ibus.events.UnknownErrorEvent;

import javax.inject.Inject;


public class MainActivity extends CleanActivity {

    @Inject
    protected ProgressUtils progressUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onEvent(InProgressEvent event) {
        progressUtils.showProgress(this, event.loadingMsg);
    }

    public void onEvent(ProgressFinishedEvent event) {
        progressUtils.hideProgress();
    }

    public void onEvent(UnknownErrorEvent event) {
        progressUtils.hideProgress();
        final String msg = "Unknown Error occurred: " + event.errorCode + " " + event.description;
        L.d(msg);
    }

    public void onEvent(UnauthorizedErrorEvent event) {
        final String msg = "Session timeout. Please log in again";
        L.d(msg);
    }

    @Override
    public void performInjection(Context context) {
        ((App)getApplication()).getAppComponent().inject(this);
    }
}
