package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.RawMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RawMatchRepository extends JpaRepository<RawMatchEntity, Long> {

    boolean existsByMatchId(Long matchId);

    Optional<RawMatchEntity> findByMatchId(Long matchId);

    List<RawMatchEntity> findByParseStatus(String parseStatus);

    @Query("select r.matchId from RawMatchEntity r")
    List<Long> findAllMatchIds();

    @Query("select r.matchId from RawMatchEntity r where r.parseStatus = :parseStatus")
    List<Long> findMatchIdsByParseStatus(@Param("parseStatus") String parseStatus);

    @Query(
            value = """
            SELECT rm.match_id
            FROM raw_matches rm
            LEFT JOIN match_events me ON me.match_id = rm.match_id
            WHERE rm.parse_status = 'PARSED'
            GROUP BY rm.match_id
            HAVING COUNT(me.id) = 0
            ORDER BY rm.match_id
            """,
            nativeQuery = true
    )
    List<Long> findParsedMatchIdsWithoutEvents();
}