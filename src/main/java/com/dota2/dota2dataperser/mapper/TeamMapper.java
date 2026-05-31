package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaTeamDto;
import com.dota2.dota2dataperser.entity.TeamEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamEntity toEntity(OpenDotaTeamDto dto) {
        if (dto == null || dto.getTeamId() == null) {
            return null;
        }

        TeamEntity entity = new TeamEntity();

        entity.setTeamId(dto.getTeamId());
        entity.setName(dto.getName() != null ? dto.getName() : "Unknown team " + dto.getTeamId());
        entity.setTag(dto.getTag());

        return entity;
    }

    public TeamEntity unknownTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }

        TeamEntity entity = new TeamEntity();

        entity.setTeamId(teamId);
        entity.setName("Unknown team " + teamId);

        return entity;
    }
}