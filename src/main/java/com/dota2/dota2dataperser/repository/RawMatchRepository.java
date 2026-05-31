package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.RawMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RawMatchRepository extends JpaRepository<RawMatchEntity, Long> {

    boolean existsByMatchId(Long matchId);

    Optional<RawMatchEntity> findByMatchId(Long matchId);

    List<RawMatchEntity> findByParseStatus(String parseStatus);
}