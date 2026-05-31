package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.PickBanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PickBanRepository extends JpaRepository<PickBanEntity, Long> {

    List<PickBanEntity> findByMatchIdOrderByOrderNumAsc(Long matchId);

    List<PickBanEntity> findByHeroId(Integer heroId);

    List<PickBanEntity> findByTeamId(Long teamId);

    boolean existsByMatchIdAndOrderNum(Long matchId, Integer orderNum);

    void deleteByMatchId(Long matchId);
}