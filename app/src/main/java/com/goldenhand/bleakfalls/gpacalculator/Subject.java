package com.goldenhand.bleakfalls.gpacalculator;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable {
    String mName;
    String mGpa;
    ArrayList<Result> mResults = new ArrayList<Result>();

    public Subject(String name, String gpa, ArrayList<Result> results) {
        mName = name;
        mGpa = gpa;
        mResults = results;
    }

    public void setName(String newName) {
        mName = newName;
    }

    public String getName() {
        return mName;
    }

    public void setGpa(String newGpa) {
        mGpa = newGpa;
    }

    public String getGpa() {
        return mGpa;
    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }

    public ArrayList<Result> getResults() {
        return mResults;
    }
}
