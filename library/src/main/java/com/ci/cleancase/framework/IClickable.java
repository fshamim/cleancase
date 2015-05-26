package com.ci.cleancase.framework;

import android.view.View;

import lombok.Data;

/**
 * Interface for the Registration of Objects which are interested in receiving the onClick Events
 * Created by fshamim on 15.04.14.
 */
public interface IClickable {
    /**
     * OnClick EventListener method that is to be defined in the xml files.
     *
     * @param view widget on which the click event is produced
     */
    void onClick(View view);

    /**
     * All IClickable implementation should post OnClickEvent to all the subscriber of this Event
     * Created by fshamim on 23.04.15.
     */
    @Data
    class ClickEvent {
        public final View view;

        public ClickEvent(View view) {
            this.view = view;
        }
    }
}

