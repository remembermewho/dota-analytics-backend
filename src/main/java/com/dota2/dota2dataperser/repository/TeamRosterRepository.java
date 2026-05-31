package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.TeamRosterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRosterRepository extends JpaRepository<TeamRosterEntity, Long> {

    List<TeamRosterEntity> findByTeamId(Long teamId);

    List<TeamRosterEntity> findByAccountId(Long accountId);
}