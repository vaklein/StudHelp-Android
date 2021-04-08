package com.example.p4_group12.Interface;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Social_links;

import java.util.ArrayList;

public class GlobalVariables {

    private static String name;
    private static String login;
    private static String email;
    private static String discord = "";
    private static String teams = "";
    private static String facebook = "";
    private static boolean social_network_charged = false;
    private static ArrayList<Course> courseArrayList;


    // personal informations

    public static String getEmail() { return email; }
    public static void setEmail(String username) { email = username; }

    public static String getLogin(){ return login;}
    public static void setLogin(String userlogin) { login = userlogin; }

    public static String getName() { return name; }
    public static void setName(String username) { name = username; }


    // social netwrok
    public static boolean getSocialNetwokCharged(){ return social_network_charged; }
    public static  void setSocialNetwokCharged(boolean b){ social_network_charged = b; }
    public static boolean havaASocialNetwork(){
        boolean b = false;
        if (!discord.equals("")) b = true;
        if (!teams.equals("")) b = true;
        if (!facebook.equals("")) b = true;
        return b;
    }

    public static String getDiscord() { return discord; }
    public static void setDiscord(String discord) { GlobalVariables.discord = discord; }

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

    public static ArrayList<Course> getCourses() { return courseArrayList; }
    public static void setCourses(ArrayList<Course> courses) { courseArrayList = courses; }

}

