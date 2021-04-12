package com.example.p4_group12.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Advertisement  implements Serializable {
    @Override
    public String toString() {
        return "Advertisement{" +
                "ID=" + ID +
                ", mail='" + mail + '\'' +
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
    private String title;
    private String description;
    private String type;
    private int courseID;
    private List<Tag> tags;
    private List<String> images;

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

    public Advertisement(int ID, String mail, String title, String description, List<Tag> tags, int courseID) {
        this.ID = ID;
        this.mail = mail;
        this.title = title;
        this.description = description;
        this.courseID = courseID;
        this.tags = tags;

        /* This is a testing version, it should be updated with an argument in the creator binding
        * list to List<String> each String being the url to the image in the database
        */
        this.images = new ArrayList<>();
        images.add(("https://db.valentinklein.eu:8182/advertisement_images/test1"));
        images.add(("https://db.valentinklein.eu:8182/advertisement_images/test2"));
        images.add(("https://db.valentinklein.eu:8182/advertisement_images/test3"));
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

    public List<Tag> getTags() { return this.tags; }

    public boolean hasImages() { return !images.isEmpty(); }

    public List<String> getImages() { return images; }


}
