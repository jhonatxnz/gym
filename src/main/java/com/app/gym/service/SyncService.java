package com.app.gym.service;

import com.app.gym.dto.UserSyncDto;
import com.app.gym.model.User;
import com.app.gym.model.UserProgress;
import com.app.gym.repository.UserProgressRepository;
import com.app.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final UserProgressRepository progressRepo;
    private final UserRepository userRepo;

    public void syncData(String email, UserSyncDto data) {
        User user = userRepo.findByEmail(email);

        UserProgress progress = progressRepo.findByUserEmail(email)
                .orElse(new UserProgress());

        progress.setUser(user);
        progress.setWorkoutData(data.getWorkoutData());
        progress.setHistoryData(data.getHistoryData());
        progress.setLastUpdate(LocalDateTime.now());

        progressRepo.save(progress);
    }

    public UserSyncDto loadData(String email) {
        return progressRepo.findByUserEmail(email)
                .map(progress -> {
                    UserSyncDto dto = new UserSyncDto();
                    dto.setWorkoutData(progress.getWorkoutData());
                    dto.setHistoryData(progress.getHistoryData());
                    return dto;
                })
                .orElse(null);
    }
}