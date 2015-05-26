package com.ci.cleancase.framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import lombok.Data;

/**
 * Created by fshamim on 05.11.14.
 */
public interface IFragmentContainer {

    /**
     * Subscriber interface for receiving fragment change events.
     *
     * @param event Fragment Change event
     */
    void onEvent(FragmentChangeEvent event);

    /**
     *
     */
    @Data
    class FragmentChangeEvent {
        public final Class<? extends Fragment> newFragmentClass;
        public final boolean doAnimation;
        public final Bundle arguments;

        public FragmentChangeEvent(Class<? extends Fragment> newFragmentClass, boolean anim, Bundle arguments) {
            this.newFragmentClass = newFragmentClass;
            this.doAnimation = anim;
            this.arguments = arguments;
        }
    }
}
