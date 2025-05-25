package com.example.demo.dto;

import lombok.Data;

@Data
public class InitDataRequest {
    private String query_id;
    private UserDto user;
    private String auth_date;
    private String signature;
    private String hash;
}
