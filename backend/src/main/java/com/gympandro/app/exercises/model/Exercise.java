package com.gympandro.app.exercises.model;

import java.util.UUID;
import java.time.ZonedDateTime;

public class Exercise {
    public UUID id;
    public String name;
    public String equipment;
    public String description;
    public ZonedDateTime createdAt;
    public UUID createdBy;
    public ZonedDateTime modifiedAt;
    public UUID modifiedBy;
}