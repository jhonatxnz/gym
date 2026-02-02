package com.app.gym.dto;

import lombok.Data;

@Data
public class WorkoutExerciseDTO {
    private String name;
    private int sets;
    private String reps;
    private String rest;
    private String muscleGroup;
}