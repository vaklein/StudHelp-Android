package com.example.p4_group12.Interface;

public class GlobalVariables {

    private static String name;
    private static String login;
    private static String email;
    private static String discord;
    private static String teams;
    private static String facebook;
    public static String getEmail() { return email; }
    public static void setEmail(String username) { email = username; }
    public static String getLogin(){ return login;}
    public static void setLogin(String userlogin) { login = userlogin; }
    public static String getName() { return name; }
    public static void setName(String username) { name = username; }

    public static String getDiscord() {
        return discord;
    }

    public static void setDiscord(String discord) {
        GlobalVariables.discord = discord;
    }

    public static String getTeams() {
        return teams;
    }

    public static void setTeams(String teams) {
        GlobalVariables.teams = teams;
    }

    public static String getFacebook() {
        return facebook;
    }

    public static void setFacebook(String facebook) {
        GlobalVariables.facebook = facebook;
    }
}
