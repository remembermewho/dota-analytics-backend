package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.client.OpenDotaClient;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaLeagueDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentIngestionService {

    private final OpenDotaClient openDotaClient;
    private final MatchParserService matchParserService;
    private final ReferenceDataService referenceDataService;

    public TournamentIngestionService(
            OpenDotaClient openDotaClient,
            MatchParserService matchParserService,
            ReferenceDataService referenceDataService
    ) {
        this.openDotaClient = openDotaClient;
        this.matchParserService = matchParserService;
        this.referenceDataService = referenceDataService;
    }

    public int syncLeagues() {
        String rawJson = openDotaClient.getLeaguesRawJson();

        List<OpenDotaLeagueDto> leagues = matchParserService.parseLeaguesJson(rawJson);

        referenceDataService.saveTournaments(leagues);

        return leagues.size();
    }
}
