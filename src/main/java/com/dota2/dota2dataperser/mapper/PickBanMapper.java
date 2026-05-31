package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaPickBanDto;
import com.dota2.dota2dataperser.entity.PickBanEntity;
import org.springframework.stereotype.Component;

@Component
public class PickBanMapper {

    private static final int RADIANT = 0;
    private static final int DIRE = 1;

    public PickBanEntity toEntity(OpenDotaPickBanDto dto, OpenDotaMatchDto match) {
        PickBanEntity entity = new PickBanEntity();

        entity.setMatchId(match.getMatchId());
        entity.setHeroId(dto.getHeroId());

        entity.setIsPick(dto.getIsPick());
        entity.setOrderNum(dto.getOrder());

        entity.setTeamSide(resolveTeamSide(dto.getTeam()));
        entity.setTeamId(resolveTeamId(dto.getTeam(), match));

        return entity;
    }

    private String resolveTeamSide(Integer team) {
        if (team == null) {
            return null;
        }

        if (team == RADIANT) {
            return "RADIANT";
        }

        if (team == DIRE) {
            return "DIRE";
        }

        return "UNKNOWN";
    }

    private Long resolveTeamId(Integer team, OpenDotaMatchDto match) {
        if (team == null) {
            return null;
        }

        if (team == RADIANT) {
            return match.getRadiantTeamId();
        }

        if (team == DIRE) {
            return match.getDireTeamId();
        }

        return null;
    }
}