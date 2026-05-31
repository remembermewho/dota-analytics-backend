package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaPlayerDto;
import com.dota2.dota2dataperser.entity.PlayerEntity;
import com.dota2.dota2dataperser.entity.PlayerMatchStatsEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public PlayerEntity toPlayerEntity(OpenDotaPlayerDto dto) {
        if (dto == null || dto.getAccountId() == null) {
            return null;
        }

        PlayerEntity entity = new PlayerEntity();

        entity.setAccountId(dto.getAccountId());
        entity.setNickname(dto.getNickname());

        return entity;
    }

    public PlayerMatchStatsEntity toStatsEntity(OpenDotaPlayerDto player, OpenDotaMatchDto match) {
        if (player == null || player.getAccountId() == null) {
            return null;
        }

        Boolean isRadiant = resolveIsRadiant(player);

        PlayerMatchStatsEntity entity = new PlayerMatchStatsEntity();

        entity.setMatchId(match.getMatchId());
        entity.setAccountId(player.getAccountId());
        entity.setHeroId(player.getHeroId());

        entity.setPlayerSlot(player.getPlayerSlot());
        entity.setIsRadiant(isRadiant);

        entity.setTeamId(resolveTeamId(match, isRadiant));

        entity.setKills(player.getKills());
        entity.setDeaths(player.getDeaths());
        entity.setAssists(player.getAssists());

        entity.setGpm(player.getGoldPerMin());
        entity.setXpm(player.getXpPerMin());

        entity.setNetWorth(player.getNetWorth());

        entity.setHeroDamage(player.getHeroDamage());
        entity.setTowerDamage(player.getTowerDamage());
        entity.setHeroHealing(player.getHeroHealing());

        entity.setLastHits(player.getLastHits());
        entity.setDenies(player.getDenies());

        entity.setLevel(player.getLevel());

        entity.setObsPlaced(player.getObsPlaced());
        entity.setSenPlaced(player.getSenPlaced());

        return entity;
    }

    private Boolean resolveIsRadiant(OpenDotaPlayerDto player) {
        if (player.getIsRadiant() != null) {
            return player.getIsRadiant();
        }

        if (player.getPlayerSlot() == null) {
            return null;
        }

        return player.getPlayerSlot() < 128;
    }

    private Long resolveTeamId(OpenDotaMatchDto match, Boolean isRadiant) {
        if (isRadiant == null) {
            return null;
        }

        return isRadiant ? match.getRadiantTeamId() : match.getDireTeamId();
    }
}