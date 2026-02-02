package com.app.gym.service;

import com.app.gym.dto.WorkoutDayDTO;
import com.app.gym.dto.WorkoutExerciseDTO;
import com.app.gym.dto.GenerateWorkoutRequest;
import com.app.gym.model.*;
import com.app.gym.repository.WorkoutDivisionConfigRepository;
import com.app.gym.repository.ExerciseRepository;
import com.app.gym.repository.WorkoutRuleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private ExerciseRepository exerciseRepo;
    @Mock
    private WorkoutDivisionConfigRepository divisionRepo;
    @Mock
    private WorkoutRuleRepository ruleRepo;

    @InjectMocks
    private WorkoutService workoutService;

    @Test
    @DisplayName("Should generate AB workout for Beginner (Hypertrophy) with correct rules")
    void shouldGenerateABBeginnerHypertrophyWorkout() {
        // --- 1. SCENARIO ---
        GenerateWorkoutRequest request = new GenerateWorkoutRequest();
        request.setWorkoutTypeId(1L); // AB
        request.setGoalId(1L);        // Hypertrophy
        request.setExperience("iniciante");

        // --- 2. MOCKS ---
        MuscleGroup chest = new MuscleGroup();
        chest.setId(1L);
        chest.setName("Peito");

        WorkoutDivisionConfig configDayA = new WorkoutDivisionConfig();
        configDayA.setDayLetter("A");
        configDayA.setMuscleGroup(chest);

        when(divisionRepo.findByWorkoutTypeIdOrderByDayLetterAsc(1L))
                .thenReturn(List.of(configDayA));

        Exercise benchPress = createExercise("Supino Reto", "composto", chest);
        Exercise fly = createExercise("Crucifixo", "isolado", chest);

        when(exerciseRepo.findByMuscleGroupId(1L))
                .thenReturn(Arrays.asList(benchPress, fly));

        // Rule for Compound/Beginner/Hypertrophy
        WorkoutRule compoundRule = new WorkoutRule();
        compoundRule.setSets(3);
        compoundRule.setReps("10-12");
        compoundRule.setRest("60s");

        when(ruleRepo.findByGoalIdAndExperienceAndExerciseType(1L, "iniciante", "composto"))
                .thenReturn(Optional.of(compoundRule));

        // Rule for Isolated/Beginner/Hypertrophy
        WorkoutRule isolatedRule = new WorkoutRule();
        isolatedRule.setSets(3);
        isolatedRule.setReps("10-12");
        isolatedRule.setRest("60s");

        when(ruleRepo.findByGoalIdAndExperienceAndExerciseType(1L, "iniciante", "isolado"))
                .thenReturn(Optional.of(isolatedRule));

        // --- 3. EXECUTION ---
        List<WorkoutDayDTO> result = workoutService.generateWorkout(request);

        // --- 4. ASSERTIONS ---
        assertNotNull(result);
        assertEquals(1, result.size());

        WorkoutDayDTO dayA = result.get(0);
        assertEquals("A", dayA.getLetter());
        assertEquals(2, dayA.getExercises().size());

        WorkoutExerciseDTO exercise1 = dayA.getExercises().get(0);
        assertEquals(3, exercise1.getSets());
        assertEquals("10-12", exercise1.getReps());
    }

    @Test
    @DisplayName("Should reduce 1 set if exercise is Isolated and Goal is Strength")
    void shouldApplySpecialStrengthRule() {
        // --- SCENARIO: STRENGTH (ID 3) ---
        GenerateWorkoutRequest request = new GenerateWorkoutRequest();
        request.setWorkoutTypeId(1L);
        request.setGoalId(3L);
        request.setExperience("avancado");

        MuscleGroup biceps = new MuscleGroup();
        biceps.setId(4L);
        biceps.setName("Biceps");

        WorkoutDivisionConfig configDayB = new WorkoutDivisionConfig();
        configDayB.setDayLetter("B");
        configDayB.setMuscleGroup(biceps);

        when(divisionRepo.findByWorkoutTypeIdOrderByDayLetterAsc(1L))
                .thenReturn(List.of(configDayB));

        Exercise curls = createExercise("Rosca Direta", "isolado", biceps);
        when(exerciseRepo.findByMuscleGroupId(4L)).thenReturn(List.of(curls));

        WorkoutRule strengthRule = new WorkoutRule();
        strengthRule.setSets(5);
        strengthRule.setReps("3-5");

        when(ruleRepo.findByGoalIdAndExperienceAndExerciseType(3L, "avancado", "isolado"))
                .thenReturn(Optional.of(strengthRule));

        // --- EXECUTION ---
        List<WorkoutDayDTO> result = workoutService.generateWorkout(request);

        // --- ASSERTION ---
        WorkoutExerciseDTO exDTO = result.get(0).getExercises().get(0);

        // Logic: If Strength and Isolated, sets - 1 and reps 8-12
        assertEquals(4, exDTO.getSets());
        assertEquals("8-12", exDTO.getReps());
    }

    private Exercise createExercise(String name, String type, MuscleGroup mg) {
        Exercise ex = new Exercise();
        ex.setName(name);
        ex.setType(type);
        ex.setMuscleGroup(mg);
        return ex;
    }
}