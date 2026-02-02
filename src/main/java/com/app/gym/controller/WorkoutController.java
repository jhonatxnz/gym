package com.app.gym.controller;

import com.app.gym.dto.WorkoutDayDTO;
import com.app.gym.dto.GenerateWorkoutRequest;
import com.app.gym.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout")
@CrossOrigin(origins = "*")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping("/generate")
    public ResponseEntity<List<WorkoutDayDTO>> generate(@RequestBody GenerateWorkoutRequest request) {
        return ResponseEntity.ok(workoutService.generateWorkout(request));
    }
    @PostMapping("/generate2")
    public ResponseEntity<List<WorkoutDayDTO>> generateWithVariation(@RequestBody GenerateWorkoutRequest request) {
        return ResponseEntity.ok(workoutService.generateWorkout(request));
    }
}