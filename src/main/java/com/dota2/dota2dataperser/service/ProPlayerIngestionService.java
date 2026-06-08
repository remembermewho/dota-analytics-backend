package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.client.OpenDotaClient;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaProPlayerDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProPlayerIngestionService {

    private final OpenDotaClient openDotaClient;
    private final MatchParserService matchParserService;
    private final ReferenceDataService referenceDataService;

    public ProPlayerIngestionService(
            OpenDotaClient openDotaClient,
            MatchParserService matchParserService,
            ReferenceDataService referenceDataService
    ) {
        this.openDotaClient = openDotaClient;
        this.matchParserService = matchParserService;
        this.referenceDataService = referenceDataService;
    }

    public int syncProPlayers() {
        String rawJson = openDotaClient.getProPlayersRawJson();

        List<OpenDotaProPlayerDto> proPlayers =
                matchParserService.parseProPlayersJson(rawJson);

        referenceDataService.saveProPlayers(proPlayers);

        return proPlayers.size();
    }
}