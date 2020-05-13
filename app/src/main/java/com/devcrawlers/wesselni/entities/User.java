package com.devcrawlers.wesselni.entities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;

import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private long id;
    private String firsName,lastName,email,phone;

    public User(long id, String firsName, String lastName, String email, String phone) {
        this.id = id;
        this.firsName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
