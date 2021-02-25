package me.headshot.ineedjesus.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) throws Exception {
        Field field;
        Object o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;

    }
}
