package com.goldenhand.bleakfalls.gpacalculator;

import java.io.Serializable;

public class Result implements Serializable {
    String mName;
    String mMarks;
    String mTotalMarks;
    String mWeightage;

    public Result(String name, String marks, String totalMarks, String weightage) {
        mName = name;
        mMarks = marks;
        mTotalMarks = totalMarks;
        mWeightage = weightage;
    }

    public void setResultName(String newName) {
        mName = newName;
    }
    public String getResultName() {
        return mName;
    }

    public void setMarks(String newMarks) {
        mMarks = newMarks;
    }

    public String getMarks() {
        return mMarks;
    }

    public void setTotalMarks(String newTotalMarks) {
        mTotalMarks = newTotalMarks;
    }

    public String getTotalMarks() {
        return mTotalMarks;
    }

    public void setWeightage(String newWeightage) {
        mWeightage = newWeightage;
    }

    public String getWeightage() {
        return mWeightage;
    }
}
