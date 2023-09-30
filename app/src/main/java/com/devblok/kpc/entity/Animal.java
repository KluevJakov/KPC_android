package com.devblok.kpc.entity;

import java.util.UUID;

/* сущность животного */
public class Animal {
    protected UUID id;

    protected String type;
    protected String sex;
    protected String age;
    protected String nickOrNumber;
    protected String breed; //масть или примета
    protected String owner;
    protected String avatar;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNickOrNumber() {
        return nickOrNumber;
    }

    public void setNickOrNumber(String nickOrNumber) {
        this.nickOrNumber = nickOrNumber;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Animal() {
    }

    public Animal(UUID id, String type, String sex, String age, String nickOrNumber, String breed, String owner, String avatar) {
        this.id = id;
        this.type = type;
        this.sex = sex;
        this.age = age;
        this.nickOrNumber = nickOrNumber;
        this.breed = breed;
        this.owner = owner;
        this.avatar = avatar;
    }
}
