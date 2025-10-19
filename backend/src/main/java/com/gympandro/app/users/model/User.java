package com.gympandro.app.users.model;

import java.util.UUID;

public class User {
    public UUID id;
    public String username;
    public String name;
    public String surname;
    public String phone;
    public String email;
    public String passwordHash;
    public boolean isActive;
    public UUID role_id;
    public UUID assigned_to;
    public String notes;

    // opzionale: per output esteso
    public String roleName;
}