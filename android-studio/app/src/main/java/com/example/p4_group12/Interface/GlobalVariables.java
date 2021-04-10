package com.example.p4_group12.Interface;

import com.example.p4_group12.DAO.Social_links;
import com.example.p4_group12.DAO.User;

public class GlobalVariables {

    private static User user;

    public static void setUser(User user){ GlobalVariables.user = user; }

    public static User getUser(){ return user; }

}

