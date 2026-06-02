package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.DisputeRequest;
import org.ease.gamehok.dto.DisputeResolutionRequest;
import org.ease.gamehok.dto.DisputeResponse;
import org.ease.gamehok.entity.Dispute;
import org.ease.gamehok.entity.DisputeStatus;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.entity.User;
import org.ease.gamehok.exception.ResourceNotFoundException;
import org.ease.gamehok.repository.DisputeRepository;
import org.ease.gamehok.repository.MatchRepository;
import org.ease.gamehok.repository.TeamRepository;
import org.ease.gamehok.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisputeService {

    private final DisputeRepository disputeRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public DisputeResponse createDispute(DisputeRequest request, Long userId) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + request.getMatchId()));
        
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + request.getTeamId()));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if dispute already exists for this match and team
        disputeRepository.findByMatchIdAndTeamId(request.getMatchId(), request.getTeamId())
                .ifPresent(existing -> {
                    throw new RuntimeException("Dispute already exists for this match and team");
                });

        Dispute dispute = new Dispute(match, team, user, request.getReason());
        Dispute savedDispute = disputeRepository.save(dispute);
        
        return convertToResponse(savedDispute);
    }

    public List<DisputeResponse> getAllDisputes() {
        return disputeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DisputeResponse> getDisputesByStatus(String status) {
        DisputeStatus disputeStatus = DisputeStatus.valueOf(status.toUpperCase());
        return disputeRepository.findByStatus(disputeStatus).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DisputeResponse> getDisputesByMatch(Long matchId) {
        return disputeRepository.findByMatchId(matchId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DisputeResponse> getDisputesByTeam(Long teamId) {
        return disputeRepository.findByTeamId(teamId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DisputeResponse> getDisputesByUser(Long userId) {
        return disputeRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DisputeResponse getDisputeById(Long id) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found with id: " + id));
        return convertToResponse(dispute);
    }

    public DisputeResponse resolveDispute(Long id, DisputeResolutionRequest request, Long adminId) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found with id: " + id));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

        if (dispute.getStatus() != DisputeStatus.PENDING) {
            throw new RuntimeException("Dispute has already been resolved or rejected");
        }

        DisputeStatus newStatus = DisputeStatus.valueOf(request.getStatus().toUpperCase());
        dispute.setStatus(newStatus);
        dispute.setAdminNotes(request.getAdminNotes());
        dispute.setResolvedBy(admin);

        Dispute resolvedDispute = disputeRepository.save(dispute);
        return convertToResponse(resolvedDispute);
    }

    public void deleteDispute(Long id) {
        Dispute dispute = disputeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found with id: " + id));
        disputeRepository.delete(dispute);
    }

    private DisputeResponse convertToResponse(Dispute dispute) {
        DisputeResponse response = new DisputeResponse();
        response.setId(dispute.getId());
        response.setMatchId(dispute.getMatch().getId());
        // Construct match name from team names
        String matchName = "Match #" + dispute.getMatch().getId();
        if (dispute.getMatch().getTeam1() != null && dispute.getMatch().getTeam2() != null) {
            matchName = dispute.getMatch().getTeam1().getTeamName() + " vs " + dispute.getMatch().getTeam2().getTeamName();
        }
        response.setMatchName(matchName);
        response.setTeamId(dispute.getTeam().getId());
        response.setTeamName(dispute.getTeam().getTeamName());
        response.setUserId(dispute.getUser().getId());
        response.setUserEmail(dispute.getUser().getEmail());
        response.setReason(dispute.getReason());
        response.setStatus(dispute.getStatus().name());
        response.setAdminNotes(dispute.getAdminNotes());
        if (dispute.getResolvedBy() != null) {
            response.setResolvedBy(dispute.getResolvedBy().getId());
            response.setResolvedByEmail(dispute.getResolvedBy().getEmail());
        }
        response.setCreatedAt(dispute.getCreatedAt());
        response.setUpdatedAt(dispute.getUpdatedAt());
        return response;
    }
}
