package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaLeagueDto;
import com.dota2.dota2dataperser.entity.TournamentEntity;
import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {

    public TournamentEntity toEntity(OpenDotaLeagueDto dto) {
        TournamentEntity entity = new TournamentEntity();

        entity.setLeagueId(dto.getLeagueId());
        entity.setName(dto.getName() != null ? dto.getName() : "Unknown tournament " + dto.getLeagueId());
        entity.setTier(dto.getTier());

        return entity;
    }
}