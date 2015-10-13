package com.google.code.gson.custom;

import com.google.gson.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arne on 13.10.15.
 */
public class GenericsTest {
    public static class Person {

        private String name;
        private String lastName;
        private int age;
        private ArrayList<Address> addressList;
        private Address singleAddress;

        public Person(String name,
                      String lastName,
                      int age,
                      ArrayList<Address> addressList) {
            this.name = name;
            this.lastName = lastName;
            this.age = age;
            this.addressList = addressList;
        }

        Person() {
        }


        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name: ")
                    .append(this.name).append("\n")
                    .append("lastName: ")
                    .append(this.lastName).append("\n")
                    .append("age: ")
                    .append(this.age).append("\n");

            for (Address address : this.addressList) {
                stringBuilder.append(address.toString());
            }

            return stringBuilder.toString();
        }

        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAge() {
            return age;
        }

        public List<Address> getAddressList() {
            return addressList;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setAddressList(ArrayList<Address> addressList) {
            this.addressList = addressList;
        }

        public Address getSingleAddress() {
            return singleAddress;
        }

        public void setSingleAddress(Address singleAddress) {
            this.singleAddress = singleAddress;
        }
    }

    public static class Address {

        private int zipcode;
        private String street;

        Address() {

        }

        public Address(int zipcode,
                       String street) {
            this.zipcode = zipcode;
            this.street = street;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("zipcode: ")
                    .append(this.zipcode).append("\n")
                    .append("street: ")
                    .append(this.street).append("\n");

            return stringBuilder.toString();
        }

        public int getZipcode() {
            return zipcode;
        }

        public String getStreet() {
            return street;
        }

        public void setZipcode(int zipcode) {
            this.zipcode = zipcode;
        }

        public void setStreet(String street) {
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

        Person person = new Person("Sawyer", "Bootstrapper", 23, addressList);
        person.setSingleAddress(workAddress);

        Gson objectMapper = new Gson();
        String value = objectMapper.toJson(person);

        JsonParser parser = new JsonParser();

        Assert.assertEquals(parser.parse(jsonValue), parser.parse(value));

    }

    private static String jsonValue = "{\"name\":\"Sawyer\",\"lastName\":\"Bootstrapper\",\"age\":23,\"addressList\":[{\"zipcode\":12345,\"street\":" +
            "\"Stenhammer Drive\"},{\"zipcode\":7986,\"street\":\"Market Street\"}],\"singleAddress\":{\"zipcode\":7986,\"street\":\"Market Street\"}}";

    @Test
    public void testDemarshallingWithEmbeddedObject() throws IOException {
        Gson objectMapper = new Gson();
        Person personValue = objectMapper.fromJson(jsonValue, Person.class);
        Assert.assertTrue(personValue.getSingleAddress() instanceof Address);
        Assert.assertEquals(7986, personValue.singleAddress.zipcode);
        Assert.assertEquals("Market Street", personValue.singleAddress.street);
    }

    @Test
    public void testDemarshallingSimpleFields() throws IOException {
        Gson objectMapper = new Gson();
        Person personValue = objectMapper.fromJson(jsonValue, Person.class);

        Assert.assertEquals("Sawyer", personValue.name);
        Assert.assertEquals("Bootstrapper", personValue.lastName);
        Assert.assertEquals(23, personValue.age);

    }

    @Test
    public void testDemarshallingListField() throws IOException {
        Gson objectMapper = new Gson();
        Person personValue = objectMapper.fromJson(jsonValue, Person.class);

        List<Address> addresses = personValue.addressList;
        Assert.assertEquals(2, addresses.size());
        Address firstAddress = addresses.get(0);
        Assert.assertTrue(firstAddress instanceof Address);

        Assert.assertEquals(12345, firstAddress.zipcode);
        Assert.assertEquals("Stenhammer Drive", firstAddress.street);

    }
}
