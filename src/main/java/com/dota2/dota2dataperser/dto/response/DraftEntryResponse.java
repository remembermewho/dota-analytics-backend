package com.dota2.dota2dataperser.dto.response;

public record DraftEntryResponse(
        Integer orderNum,
        String teamSide,
        Long teamId,
        String teamName,
        Integer heroId,
        String heroName,
        Boolean isPick
) {
}