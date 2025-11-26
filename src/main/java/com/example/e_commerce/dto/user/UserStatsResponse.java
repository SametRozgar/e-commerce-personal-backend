package com.example.e_commerce.dto.user;

import lombok.Data;

@Data
public class UserStatsResponse {
    private Long totalUsers;
    private Long activeUsers;
}