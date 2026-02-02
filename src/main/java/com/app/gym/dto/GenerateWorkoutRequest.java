package com.app.gym.dto;

import lombok.Data;

@Data
public class GenerateWorkoutRequest {
    private Long workoutTypeId;
    private Long goalId;
    private String experience;
    private Double weight;
    private Double height;
    private String gender;
}