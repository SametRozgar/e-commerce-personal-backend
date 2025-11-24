package com.example.e_commerce.dto.user;

import com.example.e_commerce.entity.User;
import lombok.Data;

@Data
public class UserSearchRequest {
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;
    private User.UserStatus status;
}
