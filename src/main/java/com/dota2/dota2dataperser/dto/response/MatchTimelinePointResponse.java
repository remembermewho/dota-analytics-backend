package com.dota2.dota2dataperser.dto.response;

public record MatchTimelinePointResponse(
        Integer minute,
        Integer radiantGoldAdv,
        Integer radiantXpAdv
) {
}