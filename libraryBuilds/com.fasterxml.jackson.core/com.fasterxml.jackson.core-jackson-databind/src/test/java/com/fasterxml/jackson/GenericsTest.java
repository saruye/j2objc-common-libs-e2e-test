package com.fasterxml.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        //        @JsonCreator
        public Person(String name,
                      String lastName,
                      int age,
                      ArrayList<Address> addressList) {
            this.name = name;
            this.lastName = lastName;
            this.age = age;
            this.addressList = addressList;
            System.out.println("use creator");
        }

        Person(){
            System.out.println("use notmal constructor");
        }

        // getters and setters

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

        Address(){

        }

        //        @JsonCreator
        public Address( int zipcode,
                        String street) {
            this.zipcode = zipcode;
            this.street = street;
        }

        // getters and setters

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
        System.out.println("jackson testMarshalling");
        Address homeAddress = new Address(12345, "Stenhammer Drive");
        Address workAddress = new Address(7986, "Market Street");
        ArrayList<Address> addressList = new ArrayList<>();
        addressList.add(homeAddress);
        addressList.add(workAddress);
        Person person = new Person("Sawyer", "Bootstrapper", 23, addressList);
        person.setSingleAddress(workAddress);
        // object mapper is the main object to marshall (write) data into JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(person);
        System.out.println(value);

        JsonNode expected = objectMapper.readTree(jsonValue);
        JsonNode actual = objectMapper.readTree(value);
        Assert.assertEquals(expected,actual);
        System.out.println("marshalling works");

    }

    private static String jsonValue = "{\"name\":\"Sawyer\",\"lastName\":\"Bootstrapper\",\"age\":23,\"addressList\":[{\"zipcode\":12345,\"street\":" +
            "\"Stenhammer Drive\"},{\"zipcode\":7986,\"street\":\"Market Street\"}],\"singleAddress\":{\"zipcode\":7986,\"street\":\"Market Street\"}}";

    @Test
    public void testDemarshalling() throws IOException {
        System.out.println("jackson test demarshalling");
        ObjectMapper objectMapper = new ObjectMapper();
        Person personValue = objectMapper.readValue(jsonValue, Person.class);
        System.out.println("print person");
        System.out.println(personValue.toString());
        System.out.println("print addresses");
        System.out.println(personValue.getAddressList().getClass());
        System.out.println(personValue.getAddressList().get(0).getClass());
        System.out.println(personValue.getAddressList());
        System.out.println("print back to json");
        System.out.println(objectMapper.writeValueAsString(personValue));

        System.out.println("print single address:");
        System.out.println(personValue.singleAddress.getClass());
        System.out.println(personValue.getSingleAddress().toString());

        System.out.println("asserts");


        Assert.assertEquals("Sawyer", personValue.name);
        Assert.assertEquals("Bootstrapper", personValue.lastName);
        Assert.assertEquals(23, personValue.age);

        Assert.assertEquals(7986, personValue.singleAddress.zipcode);
        Assert.assertEquals("Market Street", personValue.singleAddress.street);

        List<Address> addresses = personValue.addressList;
        Assert.assertEquals(2,addresses.size());
        Address firstAddress = addresses.get(0);
        Assert.assertEquals(12345, firstAddress.zipcode);
        Assert.assertEquals("Stenhammer Drive", firstAddress.street);
        System.out.println("demarhsalling works");


    }
}
