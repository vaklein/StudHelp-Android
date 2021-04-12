package com.example.p4_group12.DAO;

public class Social_links {
    private String phone;
    private String publicEmail;
    private String discord;
    private String teams;


    public Social_links(){
    }

    public Social_links(String phone, String publicEmail, String teams,String discord){
        this.phone=phone;
        this.publicEmail=publicEmail;
        this.teams=teams;
        this.discord=discord;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public String getTeams() {
        return teams;
    }

    public void setTeams(String teams) {
        this.teams = teams;
    }


    public void setAllSocialLinks(String phone, String publicEmail, String teams, String discord){
        this.phone = phone;
        this.publicEmail = publicEmail;
        this.teams = teams;
        this.discord = discord;
    }
}
