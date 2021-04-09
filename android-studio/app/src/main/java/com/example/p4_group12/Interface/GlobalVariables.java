package com.example.p4_group12.Interface;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Social_links;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalVariables {

    private static String name;
    private static String login;
    private static String email;
    private static Social_links social_links = new Social_links();
    private static boolean social_network_charged = false;
    private static ArrayList<Course> courseArrayList;
    private static final ArrayList<String> faculties = new ArrayList<>();


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
        if (!social_links.getDiscord().equals("")) b = true;
        if (!social_links.getTeams().equals("")) b = true;
        if (!social_links.getFacebook().equals("")) b = true;
        return b;
    }

    public static Social_links getSocial_links() {
        return social_links;
    }

    public static void setSocial_links(Social_links social_links) {
        GlobalVariables.social_links = social_links;
    }

    public static String getDiscord() { return social_links.getDiscord(); }
    public static void setDiscord(String discord) { social_links.setDiscord(discord);}

    public static String getTeams() {
        return social_links.getTeams();
    }
    public static void setTeams(String teams) {
        social_links.setDiscord(teams);
    }

    public static String getFacebook() {
        return social_links.getFacebook();
    }
    public static void setFacebook(String facebook) {
        social_links.setDiscord(facebook);
    }

    public static ArrayList<Course> getCourses() { return courseArrayList; }
    public static void setCourses(ArrayList<Course> courses) {
        courseArrayList = courses;
        for (Course course : courseArrayList) {
            if (!faculties.contains(course.getFaculty()) && !course.getFaculty().isEmpty()) {
                faculties.add(course.getFaculty());
            }
        }
        Collections.sort(faculties);
    }

    public static ArrayList<String> getFaculties() { return faculties; }

}

