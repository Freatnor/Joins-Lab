package com.example.freatnor.joins_lab;

/**
 * Created by Jonathan Taylor on 7/18/16.
 */
public class Employee {
    private String mFirstName;
    private String mLastName;
    private String mSSN;
    private int mYearOfBirth;
    private String mCity;

    public Employee(String SSN, String firstName, String lastName, int yearOfBirth, String city) {
        mFirstName = firstName;
        mLastName = lastName;
        mSSN = SSN;
        mYearOfBirth = yearOfBirth;
        mCity = city;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getSSN() {
        return mSSN;
    }

    public int getYearOfBirth() {
        return mYearOfBirth;
    }

    public String getCity() {
        return mCity;
    }
}
