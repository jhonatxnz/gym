package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "genero")
    private String gender;

    @Column(name = "altura")
    private Double height;

    @Column(name = "peso")
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "id_objetivo")
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "id_tipo_treino")
    private WorkoutType workoutType;

}