package com.example.p4_group12.DAO;

import java.io.Serializable;

public class Course implements Serializable {
    private int ID;
    private String code;
    private String teacher;
    private String name;
    private String university;

    public Course(int ID, String code, String teacher,String name,String university) {
        this.ID = ID;
        this.code = code;
        this.teacher = teacher;
        this.name = name;
        this.university = university;
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

    public String getName(){
        return this.name;
    }

    public String getUniversity(){
        return this.university;
    }

}
