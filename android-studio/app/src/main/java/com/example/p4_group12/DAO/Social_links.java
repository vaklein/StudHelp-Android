package com.example.p4_group12.DAO;

public class Social_links {
    private String discord;
    private String teams;
    private String facebook;

    public Social_links(){
    }

    public Social_links(String discord,String teams,String facebook){
        this.discord=discord;
        this.facebook=facebook;
        this.teams=teams;
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

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
