/*
 * Copyright (c) 2015 the authors of j2objc-common-libs-e2e-test
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.j2objc;


import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Issue639Test {

    @Test
    public void testListGenerics() {

        List<Address> addresses = Arrays.asList(new Address("Moonstreet 1"));
        Person person = new Person(addresses);

        Field[] fields = person.getClass().getFields();
        Assert.assertEquals(1, fields.length);
        Field addressesField = fields[0];

        Assert.assertTrue(List.class == addressesField.getType());
        Type genericType = addressesField.getGenericType();
        System.out.println("generictype: " + genericType);
        ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.make(List.class, new Type[]{Address.class}, null);
        System.out.println("created type: " + parameterizedType);

        Assert.assertEquals(parameterizedType, genericType);

    }


    public static class Person {
        public Person(List<Address> addresses) {
            this.addresses = addresses;
        }

        public List<Address> addresses;
    }

    public class Address {
        public Address(String street) {
            this.street = street;
        }

        public String street;
    }

}
