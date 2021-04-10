package com.example.p4_group12.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Advertisement  implements Serializable {
    private int ID;
    private String mail;
    private String title;
    private String description;
    private String type;
    private int courseID;
    private List<String> tags;
    private List<String> images;

    public Advertisement(int ID, String mail, String title, String description, String type, int courseID) {
        this.ID = ID;
        this.mail = mail;
        this.title = title;
        this.description = description;
        this.type = type;
        this.courseID = courseID;
        this.tags = new ArrayList<>();

        /* This is a testing version, it should be updated with an argument in the creator binding
        * list to List<String> each String being the url to the image in the database
        */
        this.images = new ArrayList<>();
        images.add(("https://db.valentinklein.eu:8182/advertisement_images/test1"));
        images.add(("https://db.valentinklein.eu:8182/advertisement_images/test2"));
        images.add(("https://db.valentinklein.eu:8182/advertisement_images/test3"));

        tags.add(type);
    }

    public int getID(){
        return this.ID;
    }

    public String getEmailAddress(){
        return this.mail;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getType() { return this.type; }

    public int getCourseID() { return this.courseID; }

    public List<String> getTags() { return this.tags; }

    public boolean hasImages() { return !images.isEmpty(); }

    public List<String> getImages() { return images; }

}
