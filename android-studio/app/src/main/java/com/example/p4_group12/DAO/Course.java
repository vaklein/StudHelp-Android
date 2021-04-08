package com.example.p4_group12.DAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Course implements Serializable {
    private int ID;
    private String code;
    private String teacher;
    private String university;
    private String name;
    private String faculty;
    private String quarter;

    public Course(int ID, String code, String teacher, String university, String name, String faculty, String quarter) {
        this.ID = ID;
        this.code = code;
        this.teacher = teacher;
        this.university = university;
        this.name = name;
        this.faculty = faculty;
        this.quarter = quarter;
    }

    public int getID(){
        return this.ID;
    }

    public String getCode(){
        return this.code;
    }

    public String getTeacher(){
        return this.teacher;
    }

    public String getUniversity(){
        return this.university;
    }

    public String getName(){
        return this.name;
    }

    public String getFaculty() { return this.faculty; }

    public String getQuarter() { return this.quarter; }
}
