package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.client.OpenDotaClient;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaHeroDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeroIngestionService {

    private final OpenDotaClient openDotaClient;
    private final MatchParserService matchParserService;
    private final ReferenceDataService referenceDataService;

    public HeroIngestionService(
            OpenDotaClient openDotaClient,
            MatchParserService matchParserService,
            ReferenceDataService referenceDataService
    ) {
        this.openDotaClient = openDotaClient;
        this.matchParserService = matchParserService;
        this.referenceDataService = referenceDataService;
    }

    public int syncHeroes() {
        String rawJson = openDotaClient.getHeroesRawJson();

        List<OpenDotaHeroDto> heroes = matchParserService.parseHeroesJson(rawJson);

        referenceDataService.saveHeroes(heroes);

        return heroes.size();
    }
}
