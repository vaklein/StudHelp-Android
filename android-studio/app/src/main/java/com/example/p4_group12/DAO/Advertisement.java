package com.example.p4_group12.DAO;

public class Advertisement {
    private int ID;
    private String username;
    private String title;
    private String description;

    public Advertisement(int ID, String username, String title, String description) {
        this.ID = ID;
        this.username = username;
        this.title = title;
        this.description = description;
    }

    public int getID(){
        return this.ID;
    }

    public String getUsername(){
        return this.username;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

}
