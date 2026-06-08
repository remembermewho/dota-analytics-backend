package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.PlayerMatchStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerMatchStatsRepository extends JpaRepository<PlayerMatchStatsEntity, Long> {

    List<PlayerMatchStatsEntity> findByMatchId(Long matchId);

    List<PlayerMatchStatsEntity> findByAccountId(Long accountId);

    List<PlayerMatchStatsEntity> findByHeroId(Integer heroId);

    List<PlayerMatchStatsEntity> findByTeamId(Long teamId);

    boolean existsByMatchIdAndAccountId(Long matchId, Long accountId);

    @Modifying(flushAutomatically = true)
    @Query("delete from PlayerMatchStatsEntity p where p.matchId = :matchId")
    void deleteByMatchId(@Param("matchId") Long matchId);
}