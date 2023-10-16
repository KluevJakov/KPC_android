package com.devblok.kpc.entity;

import java.util.Date;
import java.util.UUID;

/* сущность осмотра */

public class Inspect {
    protected UUID id;

    protected Date planDate;

    protected InspectStatus inspectStatus;

    protected Animal animal;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public InspectStatus getInspectStatus() {
        return inspectStatus;
    }

    public void setInspectStatus(InspectStatus inspectStatus) {
        this.inspectStatus = inspectStatus;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
