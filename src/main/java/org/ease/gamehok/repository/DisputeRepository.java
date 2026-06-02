package org.ease.gamehok.repository;

import org.ease.gamehok.entity.Dispute;
import org.ease.gamehok.entity.DisputeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {
    
    List<Dispute> findByStatus(DisputeStatus status);
    
    List<Dispute> findByMatchId(Long matchId);
    
    List<Dispute> findByTeamId(Long teamId);
    
    List<Dispute> findByUserId(Long userId);
    
    Optional<Dispute> findByMatchIdAndTeamId(Long matchId, Long teamId);
}
