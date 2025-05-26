package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class MyUser {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime lastLogin;
}
