package com.adjust.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ObjectInputFilterStream extends ObjectInputStream {
    static final Set<String> ALLOWED_CLASSES = new HashSet<>(Arrays.asList(
            "java.lang.Boolean",
            "java.lang.Double",
            "java.lang.Enum",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Number",
            "java.lang.String",
            "java.util.ArrayList",
            "java.util.HashMap",
            "java.util.LinkedHashMap",
            "java.util.LinkedList",
            "com.adjust.sdk.ActivityKind",
            "com.adjust.sdk.ActivityPackage",
            "com.adjust.sdk.ActivityState",
            "com.adjust.sdk.AdjustAttribution",
            "com.adjust.sdk.EventMetadata"
    ));

    public ObjectInputFilterStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws ClassNotFoundException, IOException {
        // Block any unknown classes
        if (!ALLOWED_CLASSES.contains(desc.getName())) {
            throw new InvalidClassException("Blocked deserialization", desc.getName());
        }
        return super.resolveClass(desc);
    }
}
