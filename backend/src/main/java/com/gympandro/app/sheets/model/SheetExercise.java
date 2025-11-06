package com.gympandro.app.sheets.model;

import java.util.UUID;
import java.time.ZonedDateTime;

public class SheetExercise {
    public UUID id;
    public UUID sheet_id;
    public UUID exercise_id;
    public int sets;
    public int reps;
    public String restSec;
    public String notes;
    public ZonedDateTime createdAt;
    public UUID createdBy;
    public ZonedDateTime modifiedAt;
    public UUID modifiedBy;

    // opzionale: per output esteso con join
    public String exerciseName;
    public String exerciseEquipment;
}