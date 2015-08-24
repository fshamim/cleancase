package com.ci.cleancase.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ci.ibus.IBus;
import com.ci.inject.Injectable;

import javax.inject.Inject;

import butterknife.ButterKnife;


/**
 * Created by fshamim on 23.04.15.
 */
public abstract class CleanFragment extends Fragment implements Injectable {

    @Inject
    protected IBus mBus;

    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.performInjection(getActivity());
        super.onCreate(savedInstanceState);
        mBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // NOTE: required only if activity defines  android:configChanges="orientation..." in manifest
        ViewGroup viewGroup = (ViewGroup) getView();
        // remove and inflate view again since the main activity doesn't cause the recreation
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
            viewGroup.clearAnimation();
            viewGroup.clearDisappearingChildren();
            View view = inflater.inflate(getLayoutId(), viewGroup);
            ButterKnife.inject(this, view);
        }
    }

    /**
     * Abstract method for initializing the layout with the default method overrides like
     * onCreateView etc.
     *
     * @return
     */
    public abstract int getLayoutId();
}
