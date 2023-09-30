package com.devblok.kpc.entity;

import java.util.UUID;

/* сущность справочника */
public class Book {
    protected UUID id;
    protected String title;
    protected String type;
    protected String author;
    protected String publishYear;
    protected String avatar;
    protected String text;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Book(UUID id, String title, String type, String author, String publishYear, String avatar, String text) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.author = author;
        this.publishYear = publishYear;
        this.avatar = avatar;
        this.text = text;
    }

    public Book() {
    }
}
