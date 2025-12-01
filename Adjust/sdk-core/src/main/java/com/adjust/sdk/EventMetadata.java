package com.adjust.sdk;


import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EventMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unchecked")
    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("eventSequence", (Class<Map<String,Integer>>)(Class)Map.class),
    };

    private Map<String, Integer> eventSequence;

    public EventMetadata() {
        eventSequence = new HashMap<>();
    }

    public int incrementSequenceForEvent(String eventToken) {
        @Nullable final Integer oldSequence = eventSequence.get(eventToken);

        // Sequence start at 1 when it does not exist
        final int newSequence = (oldSequence != null ? oldSequence : 0) + 1;

        eventSequence.put(eventToken, newSequence);

        return newSequence;
    }

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

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        ObjectInputStream.GetField fields = stream.readFields();
        eventSequence = Util.readObjectField(fields, "eventSequence", new HashMap<>());
    }
}
