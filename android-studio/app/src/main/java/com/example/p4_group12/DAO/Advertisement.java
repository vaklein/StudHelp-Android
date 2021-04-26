package com.example.p4_group12.DAO;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Advertisement  implements Serializable, Comparable {
    @Override
    public String toString() {
        return "Advertisement{" +
                "ID=" + ID +
                ", mail='" + mail + '\'' +
                ", user fullname=" + userFullname + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + "types are deprecated, please see the tags" + '\'' +
                ", courseID=" + courseID +
                ", tags=" + tags +
                ", images=" + images +
                '}';
    }

    private int ID;
    private String mail;
    private String userFullname;
    private String title;
    private String description;
    private String type;
    private String courseName;
    private int courseID;
    private List<Tag> tags;
    private List<String> tagValues = new ArrayList<>();
    private List<String> images;
    private Date creationDate;
    private Date lastUpdateDate;

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Advertisement(int ID, String mail, String userFullname, String title, String description, List<Tag> tags, int courseID, List<String> images, String creationDate, String lastUpdateDate) throws ParseException {
        this.ID = ID;
        this.mail = mail;
        this.userFullname = userFullname;
        this.title = title;
        this.description = description;
        this.courseID = courseID;
        this.tags = tags;
        for (Tag tag : tags) {
            tagValues.add(tag.getTagValue());
        }
        this.images = images;
        this.courseName = "This should not appear, and be the course name";

        Log.v("TimeLogs", "Creation is " + creationDate);
        this.creationDate = getLocaleDateFromString(creationDate);
        this.lastUpdateDate = getLocaleDateFromString(lastUpdateDate);
    }

    private Date getLocaleDateFromString(String str) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDefault = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdfDefault.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdfDefault.parse(str);


    }

    public int getID(){
        return this.ID;
    }

    public String getEmailAddress(){
        return this.mail;
    }

    public String getUserFullname() {return this.userFullname; }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getType() { return this.type; }

    public int getCourseID() { return this.courseID; }

    public List<Tag> getTags() { return this.tags; }

    public List<String> getTagValues() {
        return tagValues;
    }

    public boolean hasImages() {
        return !images.get(0).equals("null");
    }

    public List<String> getImages() { return images; }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!obj.getClass().getCanonicalName().equals(Advertisement.class.getCanonicalName())) {
            return false;
        }
        return this.getID() == ((Advertisement)obj).getID();
    }

    @Override
    public int compareTo(Object o) {
        if (!o.getClass().getCanonicalName().equals(Advertisement.class.getCanonicalName())) {
            throw new Error();
        }
        return this.getID() - ((Advertisement)o).getID();
    }

}
