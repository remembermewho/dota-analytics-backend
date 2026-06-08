package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {

    List<MatchEventEntity> findByMatchIdOrderByEventTimeSecondsAsc(Long matchId);

    List<MatchEventEntity> findByMatchIdAndEventTypeOrderByEventTimeSecondsAsc(
            Long matchId,
            String eventType
    );

    @Modifying(flushAutomatically = true)
    @Query("delete from MatchEventEntity e where e.matchId = :matchId")
    void deleteByMatchId(@Param("matchId") Long matchId);
}