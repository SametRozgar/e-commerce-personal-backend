package com.example.e_commerce.dto.user;

import com.example.e_commerce.entity.User;
import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    private User.Role role;
}
