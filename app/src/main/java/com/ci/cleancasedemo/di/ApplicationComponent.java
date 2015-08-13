package com.ci.cleancasedemo.di;

import com.ci.cleancasedemo.App;
import com.ci.cleancasedemo.MainActivity;
import com.ci.cleancasedemo.examplefeature.ui.ExampleWebApiUseCaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by fshamim on 26.05.15.
 */
@Singleton
@Component(
        modules = {AppModule.class}
)
public interface ApplicationComponent {
    void inject(App app);

    void inject(MainActivity activity);

    void inject(ExampleWebApiUseCaseFragment fragment);

}
