package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.MatchEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEventEntity, Long> {

    List<MatchEventEntity> findByMatchIdOrderByEventTimeSecondsAsc(Long matchId);

    List<MatchEventEntity> findByMatchIdAndEventTypeOrderByEventTimeSecondsAsc(
            Long matchId,
            String eventType
    );

    void deleteByMatchId(Long matchId);
}