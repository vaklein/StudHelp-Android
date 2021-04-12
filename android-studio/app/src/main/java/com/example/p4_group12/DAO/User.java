package com.example.p4_group12.DAO;

public class User {
    private String name;
    private String login;
    private String email;
    private Social_links social_links;


    public User(String name, String login, String email) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.social_links = null;
    }

    public User(String email){
        this.email = email;
    }

    public String getName() { return name; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public void setSocial_links(Social_links social_links){ this.social_links = social_links; }
    public Social_links getSocial_links(){ return this.social_links; }
    public boolean hasASocialNetwork(){ return social_links != null && (!social_links.getDiscord().equals("") || !social_links.getFacebook().equals("") || !social_links.getTeams().equals("")); }
    public void setName(String name) { this.name = name; }
    public void setLogin(String login) { this.login = login; }
}
