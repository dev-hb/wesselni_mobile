package com.devcrawlers.wesselni.entities;



public class Complaint {

    private int id,user_id;
    private String email,fullName,subject,message;

    public Complaint(int id, int user_id, String email, String fullName, String subject, String message) {
        this.id = id;
        this.user_id = user_id;
        this.email = email;
        this.fullName = fullName;
        this.subject = subject;
        this.message = message;
    }

    public Complaint() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
