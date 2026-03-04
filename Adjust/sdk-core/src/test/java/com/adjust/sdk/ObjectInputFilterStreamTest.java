package com.adjust.sdk;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ObjectInputFilterStreamTest {

    @Test
    public void allowedClasses_shouldDeserialize() throws Exception {
        String input = "hello";

        byte[] data = serialize(input);
        Object result = deserialize(data);

        assertEquals("hello", result);
    }

    @Test
    public void disallowedClasses_shouldBeBlocked() throws Exception {
        UnfilteredClass.DESERIALIZED = false;

        byte[] data = serialize(new UnfilteredClass());

        assertThrows(InvalidClassException.class, () -> {
            deserialize(data);
        });

        assertFalse(UnfilteredClass.DESERIALIZED);
    }

    @Test
    public void nestedUnfilteredObject_shouldBeBlocked() throws Exception {
        UnfilteredClass.DESERIALIZED = false;

        ArrayList<Object> list = new ArrayList<>();
        list.add(new UnfilteredClass());

        byte[] data = serialize(list);

        assertThrows(InvalidClassException.class, () -> {
            deserialize(data);
        });

        assertFalse(UnfilteredClass.DESERIALIZED);
    }

    @Test
    public void nestedSerializableFieldsMustBeWhitelisted() {
        Set<String> missing = new HashSet<>();

        for (String allowed : ObjectInputFilterStream.ALLOWED_CLASSES) {
            try {
                Class<?> cls = Class.forName(allowed);
                checkSerializableFields(cls, missing);
            } catch (ClassNotFoundException e) {
                fail("Whitelisted class not found: " + allowed);
            }
        }

        assertTrue(
                "Found serializable types in allowed classes that are missing from whitelist: " + missing,
                missing.isEmpty()
        );
    }

    private void checkSerializableFields(Class<?> cls, Set<String> missing) {
        for (Field field : cls.getDeclaredFields()) {

            int mods = field.getModifiers();

            // skip static fields
            if (Modifier.isStatic(mods)) continue;

            // skip synthetic fields
            if (field.isSynthetic()) continue;

            Class<?> type = field.getType();

            // skip primitives
            if (type.isPrimitive()) continue;

            // skip arrays for simplicity (or handle arrays separately)
            if (type.isArray()) type = type.getComponentType();

            // check if Serializable
            if (Serializable.class.isAssignableFrom(type)) {
                String typeName = type.getName();
                if (!ObjectInputFilterStream.ALLOWED_CLASSES.contains(typeName)) {
                    missing.add(cls.getName() + "." + field.getName() + " -> " + typeName);
                }
            }
        }
    }

    byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        return bos.toByteArray();
    }

    Object deserialize(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputFilterStream(bis);
        return ois.readObject();
    }
}
