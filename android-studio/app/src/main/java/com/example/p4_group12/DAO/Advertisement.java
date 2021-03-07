package com.example.p4_group12.DAO;

import java.io.Serializable;

public class Advertisement  implements Serializable {
    private int ID;
    private String mail;
    private String title;
    private String description;
    private String type;

    /* TODO : Add the type which is currently hardcoded */
    public Advertisement(int ID, String mail, String title, String description) {
        this.ID = ID;
        this.mail = mail;
        this.title = title;
        this.description = description;
        this.type = "Type";
    }

    public int getID(){
        return this.ID;
    }

    public String getUsername(){
        return this.mail;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getType() { return this.type; }
}
