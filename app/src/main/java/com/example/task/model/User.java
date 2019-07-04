package com.example.task.model;

public class User {

    private int id;
    private  String name;
    private  String phone;
    private  String email;
    private  String api_token;
    private  String image;

    public User(int id, String name, String phone, String email, String api_token) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.api_token = api_token;

    }

    public  String getImage() {
        return image;
    }

    public  String getName() {
        return name;
    }

    public  String getPhone() {
        return phone;
    }


    public  String getEmail() {
        return email;
    }

    public  String getApi_token() {
        return api_token;
    }

    public int getId() {
        return id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
