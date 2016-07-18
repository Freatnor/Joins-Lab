package com.example.freatnor.joins_lab;

/**
 * Created by Jonathan Taylor on 7/18/16.
 */
public class Job {
    private String mSSN;
    private String mCompany;
    private int mSalary;
    private int mExperience;

    public Job(String SSN, String company, int salary, int experience) {
        mSSN = SSN;
        mCompany = company;
        mSalary = salary;
        mExperience = experience;
    }

    public String getSSN() {
        return mSSN;
    }

    public String getCompany() {
        return mCompany;
    }

    public int getSalary() {
        return mSalary;
    }

    public int getExperience() {
        return mExperience;
    }
}
