package com.app.gym.service;

import com.app.gym.dto.WorkoutDayDTO;
import com.app.gym.dto.WorkoutExerciseDTO;
import com.app.gym.dto.GenerateWorkoutRequest;
import com.app.gym.model.WorkoutDivisionConfig;
import com.app.gym.model.Exercise;
import com.app.gym.model.WorkoutRule;
import com.app.gym.repository.WorkoutDivisionConfigRepository;
import com.app.gym.repository.ExerciseRepository;
import com.app.gym.repository.GoalRepository;
import com.app.gym.repository.WorkoutRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final ExerciseRepository exerciseRepo;
    private final WorkoutDivisionConfigRepository divisionRepo;
    private final WorkoutRuleRepository ruleRepo;
    private final GoalRepository goalRepo;

    public List<WorkoutDayDTO> generateWorkout(GenerateWorkoutRequest request) {

        List<WorkoutDivisionConfig> divisionConfigs = divisionRepo.findByWorkoutTypeIdOrderByDayLetterAsc(request.getWorkoutTypeId());

        Map<String, List<WorkoutDivisionConfig>> groupedDays = divisionConfigs.stream()
                .collect(Collectors.groupingBy(WorkoutDivisionConfig::getDayLetter));

        List<WorkoutDayDTO> workoutPlan = new ArrayList<>();
        List<String> sortedLetters = new ArrayList<>(groupedDays.keySet());
        Collections.sort(sortedLetters);

        for (String letter : sortedLetters) {
            List<WorkoutDivisionConfig> dayGroups = groupedDays.get(letter);
            WorkoutDayDTO dayDTO = new WorkoutDayDTO();
            dayDTO.setLetter(letter);

            String groupNames = dayGroups.stream()
                    .map(c -> c.getMuscleGroup().getName())
                    .collect(Collectors.joining(" + "));
            dayDTO.setGroupNames(groupNames);
            dayDTO.setExercises(new ArrayList<>());

            int numGroupsInDay = dayGroups.size();
            int baseExercisesPerGroup = calculateExerciseCount(request.getExperience(), numGroupsInDay);

            for (WorkoutDivisionConfig config : dayGroups) {
                Long groupId = config.getMuscleGroup().getId();

                int finalNumExercises = applyGenderVolumeAdjustment(
                        baseExercisesPerGroup,
                        request.getGender(),
                        groupId
                );

                List<Exercise> allExercises = exerciseRepo.findByMuscleGroupId(groupId);
                Collections.shuffle(allExercises);

                List<Exercise> compounds = allExercises.stream().filter(e -> e.getType().equals("composto")).toList();
                List<Exercise> isolated = allExercises.stream().filter(e -> e.getType().equals("isolado")).toList();

                List<Exercise> selected = new ArrayList<>();

                int compoundQty = Math.min((int) Math.ceil(finalNumExercises / 2.0), compounds.size());
                selected.addAll(compounds.subList(0, compoundQty));

                int remaining = finalNumExercises - selected.size();
                int isolatedQty = Math.min(remaining, isolated.size());
                selected.addAll(isolated.subList(0, isolatedQty));

                for (Exercise ex : selected) {
                    WorkoutExerciseDTO exDTO = applyRules(ex, request);
                    dayDTO.getExercises().add(exDTO);
                }
            }
            workoutPlan.add(dayDTO);
        }
        return workoutPlan;
    }

    private int calculateExerciseCount(String experience, int numGroups) {
        int base = switch (experience.toLowerCase()) {
            case "iniciante" -> 3;
            case "intermediario" -> 4;
            default -> 5;
        };
        return Math.max(2, (base / numGroups) + 1);
    }

    private int applyGenderVolumeAdjustment(int baseCount, String gender, Long muscleGroupId) {
        if (gender == null) return baseCount;

        String g = gender.toLowerCase().trim();

        if (g.equals("feminino") || g.equals("female")) {
            if (muscleGroupId == 6) {
                return baseCount + 1;
            }
        }

        if (g.equals("masculino") || g.equals("male")) {
            if (muscleGroupId == 1 || muscleGroupId == 3) {
                return baseCount + 1;
            }
        }

        return baseCount;
    }

    private WorkoutExerciseDTO applyRules(Exercise ex, GenerateWorkoutRequest request) {
        WorkoutRule rule = ruleRepo.findByGoalIdAndExperienceAndExerciseType(
                request.getGoalId(), request.getExperience(), ex.getType()
        ).orElseThrow(() -> new RuntimeException("Rule not found for profile"));

        WorkoutExerciseDTO dto = new WorkoutExerciseDTO();
        dto.setName(ex.getName());
        dto.setMuscleGroup(ex.getMuscleGroup().getName());

        String finalRest = adjustRestBasedOnBMI(rule.getRest(), request.getWeight(), request.getHeight());
        dto.setRest(finalRest);

        boolean isStrength = request.getGoalId() == 3;
        if (ex.getType().equals("isolado") && isStrength) {
            dto.setSets(rule.getSets() - 1);
            dto.setReps("8-12");
        } else {
            dto.setSets(rule.getSets());
            dto.setReps(rule.getReps());
        }
        return dto;
    }

    private String adjustRestBasedOnBMI(String originalRest, Double weight, Double height) {
        // Validação básica
        if (weight == null || height == null || height == 0) {
            return originalRest;
        }

        // --- CORREÇÃO AUTOMÁTICA DE ALTURA (CM para Metros) ---
        // Se a altura for maior que 3.0 (ex: 170, 180), assumimos que é CM.
        double heightInMeters = height;
        if (height > 3.0) {
            heightInMeters = height / 100.0; // Converte 170 -> 1.70
        }

        // Cálculo do IMC usando a altura corrigida
        double bmi = weight / (heightInMeters * heightInMeters);

        // Debug (Opcional: para você ver no console se a conta bateu)
        System.out.println("Peso: " + weight + " | Altura: " + heightInMeters + " | IMC Calculado: " + bmi);

        // Regra: Se Obesidade (IMC >= 30), aumenta o descanso
        if (bmi >= 30.0) {
            try {
                String numberOnly = originalRest.replaceAll("[^0-9-]", "");

                if (numberOnly.contains("-")) {
                    String[] parts = numberOnly.split("-");
                    int maxRest = Integer.parseInt(parts[1]);
                    return (maxRest + 30) + "s (IMC Ajustado)";
                } else {
                    int restSec = Integer.parseInt(numberOnly);
                    return (restSec + 30) + "s (IMC Ajustado)";
                }
            } catch (Exception e) {
                return originalRest + " + 30s";
            }
        }

        return originalRest;
    }
}