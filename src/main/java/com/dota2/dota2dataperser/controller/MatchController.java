package com.dota2.dota2dataperser.controller;

import com.dota2.dota2dataperser.dto.response.MatchFullResponse;
import com.dota2.dota2dataperser.service.MatchPageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "http://localhost:5173")
public class MatchController {

    private final MatchPageService matchPageService;

    public MatchController(MatchPageService matchPageService) {
        this.matchPageService = matchPageService;
    }

    @GetMapping("/{matchId}/full")
    public MatchFullResponse getFullMatch(@PathVariable Long matchId) {
        return matchPageService.getFullMatch(matchId);
    }
}