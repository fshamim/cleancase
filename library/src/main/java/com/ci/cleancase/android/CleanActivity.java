package com.ci.cleancase.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ci.cleancase.BuildConfig;
import com.ci.cleancase.framework.IClickable;
import com.ci.cleanlog.L;
import com.ci.ibus.IBus;
import com.ci.inject.Injectable;

import javax.inject.Inject;


/**
 * Created by fshamim on 23.04.15.
 */
public abstract class CleanActivity extends AppCompatActivity implements IClickable, Injectable {

    @Inject
    protected IBus mBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.performInjection(this);
        super.onCreate(savedInstanceState);
        mBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    public void onClick(View view) {
        if (BuildConfig.DEBUG) {
            L.d("onClick");
        }
        mBus.post(new IClickable.ClickEvent(view));
    }
}
