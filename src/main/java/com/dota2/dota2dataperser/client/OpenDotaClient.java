package com.dota2.dota2dataperser.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType.*;

@Component
public class OpenDotaClient {

    private final RestClient restClient;

    public OpenDotaClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getMatchRawJson(long matchId) {
        return restClient.get()
                .uri("/matches/{matchId}", matchId)
                .retrieve()
                .body(String.class);
    }

    public String getLeagueMatchesRawJson(long leagueId) {
        return restClient.get()
                .uri("/leagues/{leagueId}/matches", leagueId)
                .retrieve()
                .body(String.class);
    }

    public String getLeaguesRawJson() {
        return restClient.get()
                .uri("/leagues")
                .retrieve()
                .body(String.class);
    }

    public String getHeroesRawJson() {
        return restClient.get()
                .uri("/heroes")
                .retrieve()
                .body(String.class);
    }


}
