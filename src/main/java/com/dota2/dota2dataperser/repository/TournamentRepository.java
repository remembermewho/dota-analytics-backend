package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {

    boolean existsByLeagueId(Long leagueId);
}