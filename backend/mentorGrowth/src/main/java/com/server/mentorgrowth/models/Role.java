package com.server.mentorgrowth.models;

import lombok.Getter;

@Getter
public enum Role {
    MENTOR,
    MENTEE,
    ADMIN;

    private String role;

    public void setRole(String role) {
        this.role = role;
    }
}
