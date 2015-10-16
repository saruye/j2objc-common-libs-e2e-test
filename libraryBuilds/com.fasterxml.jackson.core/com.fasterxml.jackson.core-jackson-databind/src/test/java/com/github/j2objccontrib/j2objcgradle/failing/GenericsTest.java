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

package com.github.j2objccontrib.j2objcgradle.failing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenericsTest {
    public static class Person {

        public String name;
        public int age;
        public ArrayList<Address> addressList;
        public Address singleAddress;

        Person() {
        }

        public Person(String name,
                      int age,
                      ArrayList<Address> addressList) {
            this.name = name;
            this.age = age;
            this.addressList = addressList;
        }

    }

    public static class Address {

        public int zipcode;
        public String street;

        Address() {
        }

        public Address(int zipcode,
                       String street) {
            this.zipcode = zipcode;
            this.street = street;
        }

    }

    @Test
    public void testMarshalling() throws IOException {
        Address homeAddress = new Address(12345, "Stenhammer Drive");
        Address workAddress = new Address(7986, "Market Street");
        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(homeAddress);
        addressList.add(workAddress);
        Person person = new Person("Sawyer", 23, addressList);
        person.singleAddress=workAddress;

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(person);

        JsonNode expected = objectMapper.readTree(jsonValue);
        JsonNode actual = objectMapper.readTree(value);
        Assert.assertEquals(expected, actual);

    }

    private static String jsonValue = "{\"name\":\"Sawyer\",\"age\":23,\"addressList\":[{\"zipcode\":12345,\"street\":" +
            "\"Stenhammer Drive\"},{\"zipcode\":7986,\"street\":\"Market Street\"}],\"singleAddress\":{\"zipcode\":7986,\"street\":\"Market Street\"}}";


    @Test
    public void testDemarshallingWithEmbeddedObject() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person personValue = objectMapper.readValue(jsonValue, Person.class);
        Assert.assertTrue(personValue.singleAddress instanceof Address);
        Assert.assertEquals(7986, personValue.singleAddress.zipcode);
        Assert.assertEquals("Market Street", personValue.singleAddress.street);
    }

    @Test
    public void testDemarshallingSimpleFields() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person personValue = objectMapper.readValue(jsonValue, Person.class);

        Assert.assertEquals("Sawyer", personValue.name);
        Assert.assertEquals(23, personValue.age);

    }

    // This tests fails due to a bug in j2objc (https://github.com/google/j2objc/issues/639)
    @Test
    public void testDemarshallingListField() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person personValue = objectMapper.readValue(jsonValue, Person.class);

        List<Address> addresses = personValue.addressList;
        Assert.assertEquals(2, addresses.size());
        Address firstAddress = addresses.get(0);
        Assert.assertTrue(firstAddress instanceof Address);

        Assert.assertEquals(12345, firstAddress.zipcode);
        Assert.assertEquals("Stenhammer Drive", firstAddress.street);

    }
}
