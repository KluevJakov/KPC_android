package com.devblok.kpc.entity;

import java.util.UUID;

public class Sick {
    protected UUID id;
    protected String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sick(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public Sick() {
    }
}
