package com.google.hangouts.hypnosture.model;

import com.google.hangouts.hypnosture.util.Helpers;

/**
 * Created by bone on 08/03/2018.
 */

public class Statistics {
    public Integer improper_posture;
    public Long timestamp;

    public Statistics() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Statistics(Integer improper_posture) {
        this.improper_posture = improper_posture;
        this.timestamp = Helpers.getCurrentTimestamp().getTime();
    }
}
