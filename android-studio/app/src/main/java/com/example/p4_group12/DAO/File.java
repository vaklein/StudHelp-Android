package com.example.p4_group12.DAO;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class File {
    private int id;
    private int course_id;
    private String file;
    private String title;
    private String email;
    private Date created_at;

    File(int id, int course_id, String file, String title, String email, String created_at) throws ParseException {
        this.id = id;
        this.course_id = course_id;
        this.file = file;
        this.title = title;
        this.email = email;
        this.created_at = getLocaleDateFromString(created_at);
    }

    private Date getLocaleDateFromString(String str) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDefault = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdfDefault.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdfDefault.parse(str);
    }

    public int getId() {
        return id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getEmail() {
        return email;
    }

    public String getFile() {
        return file;
    }

    public String getTitle() {
        return title;
    }
}
