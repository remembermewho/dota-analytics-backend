package com.dota2.dota2dataperser.dto.response;

public record MatchEventResponse(
        Integer eventTimeSeconds,
        String eventTimeMinSec,
        String eventType,
        String teamSide,
        Long teamId,
        String teamName,
        Long accountId,
        String playerName,
        Integer heroId,
        String heroName,
        String description
) {
}