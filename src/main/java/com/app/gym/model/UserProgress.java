package com.app.gym.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "progresso_usuario")
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "dados_treino", columnDefinition = "LONGTEXT")
    private String workoutData;

    @Lob
    @Column(name = "dados_historico", columnDefinition = "LONGTEXT")
    private String historyData;

    @Column(name = "data_atualizacao")
    private LocalDateTime lastUpdate;
}