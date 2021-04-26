package com.example.p4_group12.Interface;

import com.example.p4_group12.DAO.Course;
import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.DAO.User;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalVariables {


    private static User user;

    private static ArrayList<Course> courseArrayList;
    private static final ArrayList<String> faculties = new ArrayList<>();

    private static boolean isTokenAlreadySent = false;

    public static void setUser(User user){ GlobalVariables.user = user; }


    public static User getUser(){ return user; }

    public static ArrayList<Course> getCourses() { return (ArrayList<Course>) courseArrayList.clone(); }
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

    public static void tokenSent(){isTokenAlreadySent = true;}

    public static void revokeToken(){isTokenAlreadySent = false;}

    public static boolean tokenAlreadySent(){return  isTokenAlreadySent;}

}

