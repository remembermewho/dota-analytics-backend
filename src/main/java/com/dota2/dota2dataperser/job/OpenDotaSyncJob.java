package com.dota2.dota2dataperser.job;

import com.dota2.dota2dataperser.service.HeroIngestionService;
import com.dota2.dota2dataperser.service.LeagueMatchDiscoveryService;
import com.dota2.dota2dataperser.service.MatchIngestionService;
import com.dota2.dota2dataperser.service.TournamentIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConditionalOnProperty(
        prefix = "opendota.sync",
        name = "enabled",
        havingValue = "true"
)
public class OpenDotaSyncJob {

    private static final Logger log = LoggerFactory.getLogger(OpenDotaSyncJob.class);

    private final TournamentIngestionService tournamentIngestionService;
    private final HeroIngestionService heroIngestionService;
    private final LeagueMatchDiscoveryService leagueMatchDiscoveryService;
    private final MatchIngestionService matchIngestionService;

    private final AtomicBoolean referenceDataSynced = new AtomicBoolean(false);

    @Value("${opendota.sync.league-ids:}")
    private String leagueIdsRaw;

    @Value("${opendota.sync.force-reload:false}")
    private boolean forceReload;

    public OpenDotaSyncJob(
            TournamentIngestionService tournamentIngestionService,
            HeroIngestionService heroIngestionService,
            LeagueMatchDiscoveryService leagueMatchDiscoveryService,
            MatchIngestionService matchIngestionService
    ) {
        this.tournamentIngestionService = tournamentIngestionService;
        this.heroIngestionService = heroIngestionService;
        this.leagueMatchDiscoveryService = leagueMatchDiscoveryService;
        this.matchIngestionService = matchIngestionService;
    }

    @Scheduled(
            initialDelayString = "${opendota.sync.initial-delay-ms:5000}",
            fixedDelayString = "${opendota.sync.fixed-delay-ms:600000}"
    )
    public void sync() {
        List<Long> leagueIds = parseLeagueIds();

        if (leagueIds.isEmpty()) {
            log.warn("OpenDota sync skipped: no league ids configured");
            return;
        }

        syncReferenceDataOnce();

        for (Long leagueId : leagueIds) {
            syncLeague(leagueId);
        }
    }

    private void syncReferenceDataOnce() {
        if (!referenceDataSynced.compareAndSet(false, true)) {
            return;
        }

        try {
            int heroesCount = heroIngestionService.syncHeroes();
            log.info("OpenDota heroes synced. count={}", heroesCount);
        } catch (Exception e) {
            log.error("Failed to sync heroes", e);
        }

        try {
            int leaguesCount = tournamentIngestionService.syncLeagues();
            log.info("OpenDota leagues synced. count={}", leaguesCount);
        } catch (Exception e) {
            log.error("Failed to sync leagues", e);
        }
    }

    private void syncLeague(Long leagueId) {
        try {
            log.info("Start syncing league. leagueId={}", leagueId);

            List<Long> matchIds = leagueMatchDiscoveryService.getMatchIdsByLeague(leagueId);

            log.info("League matches discovered. leagueId={}, count={}", leagueId, matchIds.size());

            for (Long matchId : matchIds) {
                MatchIngestionService.IngestionResult result =
                        matchIngestionService.ingestMatch(matchId, forceReload);

                if (result.success()) {
                    log.info("Match ingested. matchId={}", matchId);
                } else if (result.skipped()) {
                    log.info("Match skipped. matchId={}, reason={}", matchId, result.message());
                } else {
                    log.warn("Match ingestion failed. matchId={}, error={}", matchId, result.error());
                }

            }

            log.info("League sync finished. leagueId={}", leagueId);

        } catch (Exception e) {
            log.error("League sync failed. leagueId={}", leagueId, e);
        }
    }

    private List<Long> parseLeagueIds() {
        if (leagueIdsRaw == null || leagueIdsRaw.isBlank()) {
            return List.of();
        }

        return Arrays.stream(leagueIdsRaw.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(Long::parseLong)
                .toList();
    }

}
