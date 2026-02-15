package com.app.gym.service;

import com.app.gym.dto.CreateUserRequestDto;
import com.app.gym.model.Goal;
import com.app.gym.model.WorkoutType;
import com.app.gym.model.User;
import com.app.gym.repository.GoalRepository;
import com.app.gym.repository.WorkoutTypeRepository;
import com.app.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final GoalRepository goalRepo;
    private final WorkoutTypeRepository workoutTypeRepo;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequestDto createUserRequest) {

        if (userRepo.findByEmail(createUserRequest.getEmail()) != null) {
            throw new RuntimeException("Email já cadastrado!");
        }

        Goal goal = goalRepo.findById(createUserRequest.getGoalId())
                .orElseThrow(() -> new RuntimeException("Goal not found ID: " + createUserRequest.getGoalId()));

        WorkoutType workoutType = workoutTypeRepo.findById(createUserRequest.getWorkoutTypeId())
                .orElseThrow(() -> new RuntimeException("Workout Type not found ID: " + createUserRequest.getWorkoutTypeId()));

        User newUser = new User();

        newUser.setName(createUserRequest.getName());
        newUser.setGender(createUserRequest.getGender());
        newUser.setHeight(createUserRequest.getHeight());
        newUser.setWeight(createUserRequest.getWeight());
        newUser.setExperience(createUserRequest.getExperience());

        newUser.setEmail(createUserRequest.getEmail());
        newUser.setRole("USER");
        String encryptedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        newUser.setPassword(encryptedPassword);

        newUser.setGoal(goal);
        newUser.setWorkoutType(workoutType);

        return userRepo.save(newUser);
    }
}