package com.adjust.sdk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class UnfilteredClass implements Serializable {

    public static boolean DESERIALIZED = false;

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        in.defaultReadObject();

        // If this runs, indicates filter has FAILED with no exception thrown
        DESERIALIZED = true;
    }
}
