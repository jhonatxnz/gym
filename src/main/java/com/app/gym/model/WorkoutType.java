package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tipo_treino")
public class WorkoutType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "dias_semana", nullable = false)
    private int daysPerWeek;
}