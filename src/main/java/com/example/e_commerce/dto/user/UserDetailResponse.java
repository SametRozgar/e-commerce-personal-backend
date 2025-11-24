package com.example.e_commerce.dto.user;

import com.example.e_commerce.entity.User;

import java.time.LocalDateTime;

public class UserDetailResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;
    private User.UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer addressCount;
    private Integer orderCount;
}
