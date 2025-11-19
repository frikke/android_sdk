package com.adjust.sdk;

import com.adjust.sdk.scheduler.TimerOnce;

public class AdjustTimeoutCallback {
    private OnAdidReadListener onAdidReadListener;
    private OnAttributionReadListener onAttributionReadListener;
    private TimerOnce timeoutTimer;

    public AdjustTimeoutCallback(OnAdidReadListener onAdidReadListener) {
        this.onAdidReadListener = onAdidReadListener;
    }

    public AdjustTimeoutCallback(OnAttributionReadListener onAttributionReadListener) {
        this.onAttributionReadListener = onAttributionReadListener;
    }

    public void setOnAdidReadListener(OnAdidReadListener onAdidReadListener) {
        this.onAdidReadListener = onAdidReadListener;
    }

    public void setOnAttributionReadListener(OnAttributionReadListener onAttributionReadListener) {
        this.onAttributionReadListener = onAttributionReadListener;
    }

    public void setTimer(TimerOnce timer) {
        this.timeoutTimer = timer;
    }

    public OnAdidReadListener getOnAdidReadListener() {
        return onAdidReadListener;
    }

    public OnAttributionReadListener getOnAttributionReadListener() {
        return onAttributionReadListener;
    }

    public TimerOnce getTimeoutTimer() {
        return timeoutTimer;
    }
}
