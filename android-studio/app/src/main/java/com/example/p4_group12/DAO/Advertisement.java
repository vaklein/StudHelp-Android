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
    private List<String> tags;

    public Advertisement(int ID, String mail, String title, String description, String type) {
        this.ID = ID;
        this.mail = mail;
        this.title = title;
        this.description = description;
        this.type = type;
        this.tags = new ArrayList<>();
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

    public List<String> getTags() { return this.tags; }
}
