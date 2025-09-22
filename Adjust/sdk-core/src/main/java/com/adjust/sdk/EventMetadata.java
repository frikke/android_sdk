package com.adjust.sdk;

import java.util.HashMap;
import java.util.Map;

public class EventMetadata {
    Map<String, Integer> eventSequence;

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        EventMetadata otherEventMetadata = (EventMetadata) other;

        if (!Util.equalObject(eventSequence, otherEventMetadata.eventSequence)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode = Util.hashObject(eventSequence, hashCode);
        return hashCode;
    }

    public EventMetadata deepCopy() {
        EventMetadata newEventMetadata = new EventMetadata();
        if (this.eventSequence != null) {
            newEventMetadata.eventSequence = new HashMap<String, Integer>(this.eventSequence);
        }
        return newEventMetadata;
    }
}
