package com.adjust.sdk;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EventMetadata {
    private static final String EVENT_METADATA_FILENAME = "AdjustEventMetadata";
    private static final String EVENT_METADATA_NAME = "Event metadata";

    private @NonNull Map<String, Integer> eventSequence;
    private @NonNull final  ILogger logger;
    private @Nullable Context context;

    public EventMetadata() {
        eventSequence = new HashMap<>();
        logger = AdjustFactory.getLogger();
    }

    @SuppressWarnings("unchecked")
    public void readState(Context context) {
        this.context = context;
        try {
            Map<String, Integer> readEventSequence = Util.readObject(context,
              EVENT_METADATA_FILENAME,
              EVENT_METADATA_NAME,
              (Class<Map<String,Integer>>)(Class)Map.class);
            if (readEventSequence != null) {
                eventSequence = readEventSequence;
            } else {
                logger.error("Read null event metadata file");
            }
        } catch (Exception e) {
            logger.error("Failed to read event metadata file (%s)", e.getMessage());
        }
    }

    public int incrementSequenceForEvent(String eventToken) {
        @Nullable final Integer oldSequence = eventSequence.get(eventToken);
        final int newSequence = incrementOrStartAt1(oldSequence);

        eventSequence.put(eventToken, newSequence);
        writeEventMetadata();

        return newSequence;
    }

    private int incrementOrStartAt1(@Nullable final Integer oldValue) {
        if (oldValue == null) {
            return 1;
        }
        return oldValue + 1;
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

    private void writeEventMetadata() {
        if (context == null) {
            logger.error("Could not write event metadata before having context from reading");
            return;
        }
        Util.writeObject(eventSequence, context, EVENT_METADATA_FILENAME, EVENT_METADATA_NAME);
    }

    public static boolean deleteState(Context context) {
        return context.deleteFile(EVENT_METADATA_FILENAME);
    }
}
