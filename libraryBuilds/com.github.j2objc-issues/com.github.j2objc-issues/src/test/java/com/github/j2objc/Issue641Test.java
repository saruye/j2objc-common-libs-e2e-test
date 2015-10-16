package com.github.j2objc;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Issue641Test {

    public static class GenericClass<T> {
        void test(Map<Long, List<T>> map) {
        }
    }

    @Test
    public void testGenericMetaData() {
        for (Method m : GenericClass.class.getDeclaredMethods()) {
            if (m.getName().equals("test")) {
                Type[] paramTypes = m.getGenericParameterTypes();
                Assert.assertEquals(1,paramTypes.length);
                Assert.assertTrue(paramTypes[0] instanceof ParameterizedType);
                ParameterizedType type = (ParameterizedType) paramTypes[0];
                Type[] actualTypeArguments = type.getActualTypeArguments();
                Assert.assertEquals(2, actualTypeArguments.length);
                Assert.assertEquals(Long.class, actualTypeArguments[0]);
                Assert.assertTrue(actualTypeArguments[1] instanceof ParameterizedType);
                ParameterizedType listType = (ParameterizedType) actualTypeArguments[1];
                Assert.assertEquals(List.class,listType.getRawType());
                Assert.assertEquals(1,listType.getActualTypeArguments().length);

                System.out.println("actual type arguments: "
                        + Arrays.toString(actualTypeArguments));
            }
        }
    }
}
