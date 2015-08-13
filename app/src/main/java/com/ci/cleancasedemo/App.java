package com.ci.cleancasedemo;

import android.content.Context;

import com.ci.cleancase.android.CleanApp;
import com.ci.cleancasedemo.di.AppModule;
import com.ci.cleancasedemo.di.ApplicationComponent;
import com.ci.cleancasedemo.di.DaggerApplicationComponent;
import com.ci.cleancasedemo.examplefeature.interactor.ExampleWebApiUseCaseInteractor;

import javax.inject.Inject;

/**
 * Created by fshamim on 26.05.15.
 */
public class App extends CleanApp {

    private ApplicationComponent appComponent;

    @Inject
    ExampleWebApiUseCaseInteractor exampleWebApiUseCaseInteractor;

    @Override
    public void performInjection(Context context) {
        getAppComponent().inject(this);
    }

    public ApplicationComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerApplicationComponent.builder().appModule(new AppModule(this)).build();
        }
        return appComponent;
    }
}
