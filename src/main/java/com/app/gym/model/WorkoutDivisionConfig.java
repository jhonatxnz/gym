package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "divisao_treino_config")
public class WorkoutDivisionConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_treino_id")
    private WorkoutType workoutType;

    @Column(name = "letra_dia", nullable = false)
    private String dayLetter;

    @ManyToOne
    @JoinColumn(name = "grupo_muscular_id")
    private MuscleGroup muscleGroup;
}