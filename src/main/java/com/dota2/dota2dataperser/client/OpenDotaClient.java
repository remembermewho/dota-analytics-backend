package com.dota2.dota2dataperser.client;

import com.dota2.dota2dataperser.exception.OpenDotaApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class OpenDotaClient {

    private final RestClient restClient;
    private final OpenDotaRateLimiter rateLimiter;

    public OpenDotaClient(
            RestClient restClient,
            OpenDotaRateLimiter rateLimiter
    ) {
        this.restClient = restClient;
        this.rateLimiter = rateLimiter;
    }

    public String getMatchRawJson(long matchId) {
        return getRawJson("/matches/{matchId}", matchId);
    }

    public String getLeaguesRawJson() {
        return getRawJson("/leagues");
    }

    public String getLeagueMatchesRawJson(long leagueId) {
        return getRawJson("/leagues/{leagueId}/matches", leagueId);
    }

    public String getHeroesRawJson() {
        return getRawJson("/heroes");
    }

    private String getRawJson(String uri, Object... uriVariables) {
        rateLimiter.acquire();

        try {
            return restClient.get()
                    .uri(uri, uriVariables)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            throw new OpenDotaApiException("OpenDota API request failed. uri=" + uri, e);
        }
    }
}