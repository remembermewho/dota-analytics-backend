package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.MatchAdvantageTimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchAdvantageTimelineRepository extends JpaRepository<MatchAdvantageTimelineEntity, Long> {

    List<MatchAdvantageTimelineEntity> findByMatchIdOrderByMinuteAsc(Long matchId);

    boolean existsByMatchIdAndMinute(Long matchId, Integer minute);

    void deleteByMatchId(Long matchId);
}