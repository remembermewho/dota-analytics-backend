package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.client.OpenDotaClient;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import org.springframework.stereotype.Service;

@Service
public class MatchIngestionService { // Главный сервис для загрузки одного матча

    private final OpenDotaClient openDotaClient;
    private final RawMatchService rawMatchService;
    private final MatchParserService matchParserService;
    private final NormalizedMatchSaveService normalizedMatchSaveService;

    public MatchIngestionService(
            OpenDotaClient openDotaClient,
            RawMatchService rawMatchService,
            MatchParserService matchParserService,
            NormalizedMatchSaveService normalizedMatchSaveService
    ) {
        this.openDotaClient = openDotaClient;
        this.rawMatchService = rawMatchService;
        this.matchParserService = matchParserService;
        this.normalizedMatchSaveService = normalizedMatchSaveService;
    }

    public IngestionResult ingestMatch(long matchId) {
        return ingestMatch(matchId, false);
    }

    public IngestionResult ingestMatch(long matchId, boolean forceReload) {
        if (!forceReload && rawMatchService.isParsed(matchId)) {
            return IngestionResult.skipped(matchId, "Match already parsed");
        }

        try {
            String rawJson = openDotaClient.getMatchRawJson(matchId);

            rawMatchService.saveRawMatch(matchId, rawJson);

            OpenDotaMatchDto dto = matchParserService.parseMatchJson(rawJson);

            normalizedMatchSaveService.saveNormalizedMatch(dto);

            rawMatchService.markParsed(matchId);

            return IngestionResult.success(matchId);

        } catch (Exception e) {
            rawMatchService.markError(matchId, e.getMessage());

            return IngestionResult.failed(matchId, e.getMessage());
        }
    }

    public IngestionResult reparseRawMatch(long matchId) {
        try {
            String rawJson = rawMatchService
                    .getRawJson(matchId)
                    .orElseThrow(() -> new IllegalArgumentException("Raw match not found: " + matchId));

            OpenDotaMatchDto dto = matchParserService.parseMatchJson(rawJson);

            normalizedMatchSaveService.saveNormalizedMatch(dto);

            rawMatchService.markParsed(matchId);

            return IngestionResult.success(matchId);

        } catch (Exception e) {
            rawMatchService.markError(matchId, e.getMessage());

            return IngestionResult.failed(matchId, e.getMessage());
        }
    }

    public record IngestionResult(
            long matchId,
            boolean success,
            boolean skipped,
            String message,
            String error
    ) {
        public static IngestionResult success(long matchId) {
            return new IngestionResult(matchId, true, false, "OK", null);
        }

        public static IngestionResult skipped(long matchId, String message) {
            return new IngestionResult(matchId, false, true, message, null);
        }

        public static IngestionResult failed(long matchId, String error) {
            return new IngestionResult(matchId, false, false, "ERROR", error);
        }
    }
}
