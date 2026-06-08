package com.dota2.dota2dataperser.dto.response;

public record MatchPlayerResponse(
        Long accountId,

        String displayName,
        String nickname,
        String proNickname,

        Long teamId,
        String teamName,
        String teamSide,

        Integer heroId,
        String heroName,

        Integer kills,
        Integer deaths,
        Integer assists,

        Integer gpm,
        Integer xpm,
        Integer netWorth,

        Integer heroDamage,
        Integer towerDamage,
        Integer heroHealing,

        Integer lastHits,
        Integer denies,
        Integer level
) {
}