package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.entity.MatchEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class MatchMapper {

    public MatchEntity toEntity(OpenDotaMatchDto dto) {
        MatchEntity entity = new MatchEntity();

        entity.setMatchId(dto.getMatchId());
        entity.setLeagueId(dto.getLeagueId());
        entity.setPatchId(dto.getPatch());

        entity.setRadiantTeamId(dto.getRadiantTeamId());
        entity.setDireTeamId(dto.getDireTeamId());

        entity.setRadiantWin(dto.getRadiantWin());
        entity.setDuration(dto.getDuration());

        entity.setStartTime(toDateTime(dto.getStartTime()));

        entity.setRadiantScore(dto.getRadiantScore());
        entity.setDireScore(dto.getDireScore());

        entity.setFirstBloodTime(dto.getFirstBloodTime());
        entity.setCluster(dto.getCluster());
        entity.setGameMode(dto.getGameMode());
        entity.setLobbyType(dto.getLobbyType());
        entity.setMatchSeqNum(dto.getMatchSeqNum());

        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    private LocalDateTime toDateTime(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }

        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(epochSeconds),
                ZoneOffset.UTC
        );
    }
}