package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    boolean existsByMatchId(Long matchId);

    Optional<MatchEntity> findByMatchId(Long matchId);

    List<MatchEntity> findByLeagueId(Long leagueId);

    List<MatchEntity> findByRadiantTeamIdOrDireTeamId(Long radiantTeamId, Long direTeamId);
}