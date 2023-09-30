package com.devblok.kpc.entity;

import java.util.UUID;

/* сущность системной роли */
public class Role {
    protected UUID id;

    protected String name;
    protected String nameLocalization;

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

    public String getNameLocalization() {
        return nameLocalization;
    }

    public void setNameLocalization(String nameLocalization) {
        this.nameLocalization = nameLocalization;
    }

    public Role(UUID id, String name, String nameLocalization) {
        this.id = id;
        this.name = name;
        this.nameLocalization = nameLocalization;
    }

    public Role() {
    }
}
