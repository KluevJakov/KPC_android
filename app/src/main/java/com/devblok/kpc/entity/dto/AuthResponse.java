package com.devblok.kpc.entity.dto;

import com.devblok.kpc.entity.User;


public class AuthResponse {
    private int status;
    private User user;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
