package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.MatchAdvantageTimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchAdvantageTimelineRepository extends JpaRepository<MatchAdvantageTimelineEntity, Long> {

    List<MatchAdvantageTimelineEntity> findByMatchIdOrderByMinuteAsc(Long matchId);

    boolean existsByMatchIdAndMinute(Long matchId, Integer minute);

    @Modifying(flushAutomatically = true)
    @Query("delete from MatchAdvantageTimelineEntity t where t.matchId = :matchId")
    void deleteByMatchId(@Param("matchId") Long matchId);
}