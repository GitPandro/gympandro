package com.gympandro.app.sheets.model;

import java.util.UUID;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Sheet {
    public UUID id;
    public UUID user_id;
    public String title;
    public LocalDate startDate;
    public LocalDate endDate;
    public String status; // ACTIVE | ARCHIVED
    public ZonedDateTime createdAt;
    public UUID createdBy;
    public ZonedDateTime modifiedAt;
    public UUID modifiedBy;

    // opzionale: per output esteso con join
    public String userName;
    public String creatorName;
}