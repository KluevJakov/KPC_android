package com.devblok.kpc.entity;

import java.util.Date;
import java.util.UUID;

/* сущность осмотра */

public class Inspect {
    protected UUID id;

    protected Date planDate;

    protected InspectStatus inspectStatus;

    protected Animal animal;

}
