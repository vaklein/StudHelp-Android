package com.example.p4_group12.DAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Course implements Serializable {
    private int ID;
    private String code;
    private String name;
    private String university;
    private String fac;
    private String quadri;

    public Course(int ID, String code, String university, String name, String fac, String quadri) {
        this.ID = ID;
        this.code = code;
        this.name = name;
        this.university = university;
        this.fac = fac;
        this.quadri = quadri;
    }

    public int getID(){
        return this.ID;
    }

    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }

    public String getUniversity(){
        return this.university;
    }
}
