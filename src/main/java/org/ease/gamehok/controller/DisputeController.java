package org.ease.gamehok.controller;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.DisputeRequest;
import org.ease.gamehok.dto.DisputeResolutionRequest;
import org.ease.gamehok.dto.DisputeResponse;
import org.ease.gamehok.entity.User;
import org.ease.gamehok.repository.UserRepository;
import org.ease.gamehok.service.DisputeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disputes")
@RequiredArgsConstructor
public class DisputeController {

    private final DisputeService disputeService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<DisputeResponse> createDispute(
            @RequestBody DisputeRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Extract user ID from UserDetails (you may need to adjust this based on your JWT implementation)
        Long userId = extractUserId(userDetails);
        DisputeResponse response = disputeService.createDispute(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DisputeResponse>> getAllDisputes() {
        List<DisputeResponse> disputes = disputeService.getAllDisputes();
        return ResponseEntity.ok(disputes);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DisputeResponse>> getDisputesByStatus(@PathVariable String status) {
        List<DisputeResponse> disputes = disputeService.getDisputesByStatus(status);
        return ResponseEntity.ok(disputes);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<DisputeResponse>> getDisputesByMatch(@PathVariable Long matchId) {
        List<DisputeResponse> disputes = disputeService.getDisputesByMatch(matchId);
        return ResponseEntity.ok(disputes);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<DisputeResponse>> getDisputesByTeam(@PathVariable Long teamId) {
        List<DisputeResponse> disputes = disputeService.getDisputesByTeam(teamId);
        return ResponseEntity.ok(disputes);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisputeResponse>> getDisputesByUser(@PathVariable Long userId) {
        List<DisputeResponse> disputes = disputeService.getDisputesByUser(userId);
        return ResponseEntity.ok(disputes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisputeResponse> getDisputeById(@PathVariable Long id) {
        DisputeResponse dispute = disputeService.getDisputeById(id);
        return ResponseEntity.ok(dispute);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<DisputeResponse> resolveDispute(
            @PathVariable Long id,
            @RequestBody DisputeResolutionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long adminId = extractUserId(userDetails);
        DisputeResponse response = disputeService.resolveDispute(id, request, adminId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispute(@PathVariable Long id) {
        disputeService.deleteDispute(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return user.getId();
    }
}
