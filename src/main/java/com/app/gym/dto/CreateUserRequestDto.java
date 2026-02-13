package com.app.gym.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    @NotBlank
    private String name;

    private String gender;
    private Double height;
    private Double weight;
    private Long goalId;
    private Long workoutTypeId;

    @NotBlank
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank
    private String password;
}