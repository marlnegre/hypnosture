package com.google.hangouts.hypnosture.model;

import com.google.hangouts.hypnosture.util.Helpers;

/**
 * Created by neil on 3/13/18.
 */

public class Snore {
    public Integer snore_detected;
    public Long timestamp;

    public Snore(){

    }

    public Snore(Integer snore_detected){
        this.snore_detected = snore_detected;
        this.timestamp = Helpers.getCurrentTimestamp().getTime();
    }

}
