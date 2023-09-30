package com.devblok.kpc.entity;

import java.util.Date;
import java.util.UUID;

/* сущность пользователя */
public class User {
    protected UUID id;
    protected String fio;
    protected Date birthday;
    protected String phone;
    protected String email;
    protected String password;
    protected String avatar;
    protected boolean activated;
    protected Role role;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(UUID id, String fio, Date birthday, String phone, String email, String password, String avatar, boolean activated, Role role) {
        this.id = id;
        this.fio = fio;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.activated = activated;
        this.role = role;
    }

    public User() {
    }
}
