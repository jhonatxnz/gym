package com.app.gym.controller;

import com.app.gym.dto.UserSyncDto;
import com.app.gym.service.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @PostMapping
    public ResponseEntity<Void> uploadData(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody UserSyncDto data) {
        syncService.syncData(userDetails.getUsername(), data);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserSyncDto> downloadData(@AuthenticationPrincipal UserDetails userDetails) {
        UserSyncDto data = syncService.loadData(userDetails.getUsername());

        if (data == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(data);
    }
}