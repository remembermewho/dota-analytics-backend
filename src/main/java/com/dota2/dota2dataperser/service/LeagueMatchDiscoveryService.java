package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.client.OpenDotaClient;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaLeagueMatchDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueMatchDiscoveryService {

    private final OpenDotaClient openDotaClient;
    private final MatchParserService matchParserService;

    public LeagueMatchDiscoveryService(
            OpenDotaClient openDotaClient,
            MatchParserService matchParserService
    ) {
        this.openDotaClient = openDotaClient;
        this.matchParserService = matchParserService;
    }

    public List<OpenDotaLeagueMatchDto> getLeagueMatches(Long leagueId) {
        String rawJson = openDotaClient.getLeagueMatchesRawJson(leagueId);

        return matchParserService.parseLeagueMatchesJson(rawJson);
    }

    public List<Long> getMatchIdsByLeague(Long leagueId) {
        return getLeagueMatches(leagueId)
                .stream()
                .map(OpenDotaLeagueMatchDto::getMatchId)
                .filter(matchId -> matchId != null)
                .distinct()
                .toList();
    }
}
