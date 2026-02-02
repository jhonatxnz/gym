package com.app.gym.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonPropertyOrder({
        "groupNames",
        "letter",
        "exercises"
})
@Setter
@Getter
@Data
public class WorkoutDayDTO {

    private String groupNames;
    private String letter;
    private List<WorkoutExerciseDTO> exercises;

}
