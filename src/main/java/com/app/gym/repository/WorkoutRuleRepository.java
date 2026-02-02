package com.app.gym.repository;

import com.app.gym.model.WorkoutRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WorkoutRuleRepository extends JpaRepository<WorkoutRule, Long> {

    Optional<WorkoutRule> findByGoalIdAndExperienceAndExerciseType(
            Long goalId, String experience, String exerciseType
    );
}