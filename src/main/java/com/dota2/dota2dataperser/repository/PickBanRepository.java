package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.PickBanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickBanRepository extends JpaRepository<PickBanEntity, Long> {

    List<PickBanEntity> findByMatchIdOrderByOrderNumAsc(Long matchId);

    List<PickBanEntity> findByHeroId(Integer heroId);

    List<PickBanEntity> findByTeamId(Long teamId);

    boolean existsByMatchIdAndOrderNum(Long matchId, Integer orderNum);

    @Modifying(flushAutomatically = true)
    @Query("delete from PickBanEntity p where p.matchId = :matchId")
    void deleteByMatchId(@Param("matchId") Long matchId);
}