package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "grupo_muscular")
public class MuscleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;
}