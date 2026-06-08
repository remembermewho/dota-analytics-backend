package com.dota2.dota2dataperser.controller;

import com.dota2.dota2dataperser.service.AdminReparseService;
import com.dota2.dota2dataperser.service.MatchIngestionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/matches")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminMatchController {

    private final MatchIngestionService matchIngestionService;
    private final AdminReparseService adminReparseService;

    public AdminMatchController(
            MatchIngestionService matchIngestionService,
            AdminReparseService adminReparseService
    ) {
        this.matchIngestionService = matchIngestionService;
        this.adminReparseService = adminReparseService;
    }

    @PostMapping("/{matchId}/reparse")
    public MatchIngestionService.IngestionResult reparseMatch(
            @PathVariable Long matchId
    ) {
        return matchIngestionService.reparseRawMatch(matchId);
    }

    @PostMapping("/reparse-all")
    public AdminReparseService.ReparseSummary reparseAllMatches() {
        return adminReparseService.reparseAllRawMatches();
    }

    @PostMapping("/reparse-parsed")
    public AdminReparseService.ReparseSummary reparseParsedMatches() {
        return adminReparseService.reparseParsedMatches();
    }

    @PostMapping("/reparse-missing-events")
    public AdminReparseService.ReparseSummary reparseMatchesWithoutEvents() {
        return adminReparseService.reparseParsedMatchesWithoutEvents();
    }
}