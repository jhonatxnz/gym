package com.app.gym.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
    private String gender;
    private Double height;
    private Double weight;
    private Long goalId;
    private Long workoutTypeId;

}