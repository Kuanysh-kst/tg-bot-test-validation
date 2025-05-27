package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String first_name;
    private String last_name;
    private String username;
    private String language_code;
    private boolean allows_write_to_pm;
    private String photo_url;
}
