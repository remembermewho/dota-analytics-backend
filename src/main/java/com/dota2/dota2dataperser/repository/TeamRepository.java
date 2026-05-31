package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    boolean existsByTeamId(Long teamId);

    Optional<TeamEntity> findByTeamId(Long teamId);
}