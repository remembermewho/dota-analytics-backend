package com.dota2.dota2dataperser.dto.response;

public record MatchTeamResponse(
        Long teamId,
        String teamName,
        Integer kills,
        Boolean win
) {
}