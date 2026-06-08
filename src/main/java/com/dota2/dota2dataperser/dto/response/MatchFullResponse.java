package com.dota2.dota2dataperser.dto.response;

import java.util.List;

public record MatchFullResponse(
        Long matchId,
        String tournamentName,
        String startTime,
        Integer durationSeconds,
        String durationMinSec,

        MatchTeamResponse radiant,
        MatchTeamResponse dire,

        Integer totalKills,

        List<DraftEntryResponse> picks,
        List<DraftEntryResponse> bans,

        List<MatchPlayerResponse> players,

        List<MatchTimelinePointResponse> timeline,

        List<MatchEventResponse> events
) {
}