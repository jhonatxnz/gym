package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "regra_treino")
public class WorkoutRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "objetivo_id")
    private Goal goal;

    @Column(name = "experiencia")
    private String experience;

    @Column(name = "tipo_exercicio")
    private String exerciseType;

    @Column(name = "series")
    private int sets;

    @Column(name = "repeticoes")
    private String reps;

    @Column(name = "descanso")
    private String rest;
}