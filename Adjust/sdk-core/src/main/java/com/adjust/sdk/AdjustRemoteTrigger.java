package com.adjust.sdk;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class AdjustRemoteTrigger {
    private final String label;
    private final JSONObject payload;

    public AdjustRemoteTrigger(String label, JSONObject payload) {
        this.label = label;
        this.payload = payload;
    }

    public String getLabel() {
        return label;
    }

    public JSONObject getPayload() {
        return payload;
    }

    @NonNull
    @Override
    public String toString() {
        return "AdjustRemoteTrigger{" +
                "label='" + label + '\'' +
                ", payload=" + payload +
                '}';
    }
}
