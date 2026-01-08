package com.server.mentorgrowth.models;

import lombok.Data;

@Data
public class Experience {
    private String id;
    private String userId;
    private String domain;
    private String company;
    private String role;
    private String duration;
}
