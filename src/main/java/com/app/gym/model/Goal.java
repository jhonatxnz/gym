package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "objetivo")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "descricao")
    private String description;
}