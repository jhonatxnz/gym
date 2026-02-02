package com.app.gym.service;

import com.app.gym.dto.CreateUserRequest;
import com.app.gym.model.Goal;
import com.app.gym.model.WorkoutType;
import com.app.gym.model.User;
import com.app.gym.repository.GoalRepository;
import com.app.gym.repository.WorkoutTypeRepository;
import com.app.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final GoalRepository goalRepo;
    private final WorkoutTypeRepository workoutTypeRepo;

    public User createUser(CreateUserRequest request) {
        Goal goal = goalRepo.findById(request.getGoalId())
                .orElseThrow(() -> new RuntimeException("Goal not found ID: " + request.getGoalId()));

        WorkoutType workoutType = workoutTypeRepo.findById(request.getWorkoutTypeId())
                .orElseThrow(() -> new RuntimeException("Workout Type not found ID: " + request.getWorkoutTypeId()));

        User user = new User();
        user.setName(request.getName());       // Antes era setNome
        user.setGender(request.getGender());   // Antes era setGenero
        user.setHeight(request.getHeight());   // Antes era setAltura
        user.setWeight(request.getWeight());   // Antes era setPeso
        user.setGoal(goal);                    // Antes era setObjetivo
        user.setWorkoutType(workoutType);      // Antes era setTipoTreino

        return userRepo.save(user);
    }
}