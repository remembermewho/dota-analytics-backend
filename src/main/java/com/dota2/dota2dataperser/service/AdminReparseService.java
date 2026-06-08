package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.repository.RawMatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminReparseService {

    private final RawMatchRepository rawMatchRepository;
    private final MatchIngestionService matchIngestionService;

    public AdminReparseService(
            RawMatchRepository rawMatchRepository,
            MatchIngestionService matchIngestionService
    ) {
        this.rawMatchRepository = rawMatchRepository;
        this.matchIngestionService = matchIngestionService;
    }

    public ReparseSummary reparseAllRawMatches() {
        List<Long> matchIds = rawMatchRepository.findAllMatchIds();

        return reparseMatchIds(matchIds);
    }

    public ReparseSummary reparseParsedMatchesWithoutEvents() {
        List<Long> matchIds = rawMatchRepository.findParsedMatchIdsWithoutEvents();

        return reparseMatchIds(matchIds);
    }

    public ReparseSummary reparseParsedMatches() {
        List<Long> matchIds = rawMatchRepository.findMatchIdsByParseStatus("PARSED");

        return reparseMatchIds(matchIds);
    }

    private ReparseSummary reparseMatchIds(List<Long> matchIds) {
        int successCount = 0;
        int failedCount = 0;
        int skippedCount = 0;

        List<ReparseFailure> failures = new ArrayList<>();

        for (Long matchId : matchIds) {
            MatchIngestionService.IngestionResult result =
                    matchIngestionService.reparseRawMatch(matchId);

            if (result.success()) {
                successCount++;
            } else if (result.skipped()) {
                skippedCount++;
            } else {
                failedCount++;

                if (failures.size() < 30) {
                    failures.add(new ReparseFailure(matchId, result.error()));
                }
            }
        }

        return new ReparseSummary(
                matchIds.size(),
                successCount,
                failedCount,
                skippedCount,
                failures
        );
    }

    public record ReparseSummary(
            int totalMatches,
            int successCount,
            int failedCount,
            int skippedCount,
            List<ReparseFailure> failures
    ) {
    }

    public record ReparseFailure(
            Long matchId,
            String error
    ) {
    }
}