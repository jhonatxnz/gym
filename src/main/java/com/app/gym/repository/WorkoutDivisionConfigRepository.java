package com.app.gym.repository;

import com.app.gym.model.WorkoutDivisionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutDivisionConfigRepository extends JpaRepository<WorkoutDivisionConfig, Long> {
    List<WorkoutDivisionConfig> findByWorkoutTypeIdOrderByDayLetterAsc(Long workoutTypeId);
}