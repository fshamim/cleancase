package com.ci.cleancase.android;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import com.ci.cleanlog.L;
import com.ci.ibus.IBus;
import com.ci.ibus.events.ShowToastEvent;
import com.ci.ibus.events.TTSEvent;
import com.ci.ibus.events.VibrateEvent;
import com.ci.inject.Injectable;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.Locale;

import javax.inject.Inject;


/**
 * Created by fshamim on 24.04.15.
 */
public abstract class CleanApp extends Application implements TextToSpeech.OnInitListener, Injectable {

    protected TextToSpeech mTts;
    @Inject
    Vibrator vibrator;

    protected IBus mBus;

    @Override
    public void onCreate() {
        this.performInjection(this);
        super.onCreate();
    }

    @Inject
    public void setBus(IBus bus) {
        this.mBus = bus;
        mBus.register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        L.e("onTerminate");
        mBus.unregister(this);
    }

    /**
     * Subscribes to ShowToastEvent on the Bus
     *
     * @param event event to be processed
     */
    public void onEventMainThread(ShowToastEvent event) {
        getSuperToast(this, event).show();
    }

    /**
     * Subscribes to TTSEvent on the Bus
     *
     * @param event event to be processed
     */
    public void onEventMainThread(TTSEvent event) {
        // Speak and drop all pending entries in the playback queue.
        mTts.speak(event.getText(),
                TextToSpeech.QUEUE_FLUSH,
                null);
    }

    /**
     * Subscribes to VibrateEvent on the Bus
     *
     * @param event event to be processed
     */
    public void onEventMainThread(VibrateEvent event) {
        vibrator.vibrate(event.getDuration());
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Lanuage data is missing or the language is not supported.
                L.e("Language is not available.");
            }

        } else {
            // Initialization failed.
            L.e("Could not initialize TextToSpeech.");
        }
    }

    public static SuperToast getSuperToast(Context context, ShowToastEvent event) {
        SuperToast toast = new SuperToast(context);
        toast.setAnimations(SuperToast.Animations.POPUP);
        toast.setText(event.getText());
        toast.setDuration(event.getDuration() == ShowToastEvent.ToastDuration.LONG ? SuperToast.Duration.LONG : SuperToast.Duration.SHORT);
        int textSize = SuperToast.TextSize.EXTRA_SMALL;
        switch (event.getTextSize()) {
            case SMALL:
                textSize = SuperToast.TextSize.SMALL;
                break;
            case MEDIUM:
                textSize = SuperToast.TextSize.MEDIUM;
                break;
            case LARGE:
                textSize = SuperToast.TextSize.LARGE;
                break;
        }
        toast.setTextSize(textSize);
        int color = SuperToast.Background.BLACK;
        switch (event.getColor()) {
            case RED:
                color = SuperToast.Background.RED;
                break;
            case GREEN:
                color = SuperToast.Background.GREEN;
                break;
            case BLUE:
                color = SuperToast.Background.BLUE;
                break;
            case PURPLE:
                color = SuperToast.Background.PURPLE;
                break;
            case ORANGE:
                color = SuperToast.Background.ORANGE;
                break;
            case GREY:
                color = SuperToast.Background.GRAY;
                break;
        }
        toast.setBackground(color);
        return toast;
    }
}
