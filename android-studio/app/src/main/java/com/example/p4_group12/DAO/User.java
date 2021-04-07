package com.example.p4_group12.DAO;

public class User {
    private String name;
    private String login;
    private String email;
    private String password;


    public User(String name, String login, String email) {
        this.name = name;
        this.login = login;
        this.email = email;
    }

    public User(String name, String login, String email, String password) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
