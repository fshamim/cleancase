package com.ci.cleancasedemo.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.ci.cleancase.security.ssl.SSLSocketFactoryBuilder;
import com.ci.cleancasedemo.App;
import com.ci.cleancasedemo.R;
import com.ci.cleancasedemo.examplefeature.api.IExampleWebApi;
import com.ci.cleancasedemo.examplefeature.interactor.ExampleWebApiUseCaseInteractor;
import com.ci.ibus.IBus;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fshamim on 26.05.15.
 */
@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    App getApp() {
        return this.app;
    }

    @Provides
    @Singleton
    SharedPreferences getPrefs(App app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    @Singleton
    Vibrator getVibrator(App app) {
        return (Vibrator) app.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Provides
    @Singleton
    IBus getBus() {
        return new MyBus();
    }

    class MyBus extends EventBus implements IBus {
    }

    Scheduler getIOSchedular() {
        return Schedulers.io();
    }
    Scheduler getUISchedular() {
        return AndroidSchedulers.mainThread();
    }


    @Provides
    @Singleton
    ExampleWebApiUseCaseInteractor getExampleApiUseCaseInteractor(IBus bus, IExampleWebApi api) {
        return new ExampleWebApiUseCaseInteractor(bus, getUISchedular(), getIOSchedular(), api);
    }

    @Provides
    @Singleton
    IExampleWebApi getExample(App app) {
        final String host = "jsonplaceholder.typicode.com";
        final String endpoint = "https://" + host;
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setSslSocketFactory(SSLSocketFactoryBuilder.getSSLSocketFactory(app, R.raw.jsonplaceholderkeystore, "abcd1234"));
        okHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return host.compareTo(hostname) == 0;
            }
        });
        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(endpoint)
                .setClient(new OkClient(okHttpClient))
                .build();

        return restAdapter.create(IExampleWebApi.class);
    }
}
