package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "exercicio")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "tipo", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "grupo_muscular_id")
    private MuscleGroup muscleGroup;
}