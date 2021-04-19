package com.example.p4_group12.DAO;

public class File {
    private int id;
    private int course_id;
    private String file;
    private String title;
    private String email;
    private String created_at;

    File(int id, int course_id, String file, String title, String email, String created_at){
        this.id = id;
        this.course_id = course_id;
        this.file = file;
        this.title = title;
        this.email = email;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEmail() {
        return email;
    }

    public String getFile() {
        return file;
    }

    public String getTitle() {
        return title;
    }
}
