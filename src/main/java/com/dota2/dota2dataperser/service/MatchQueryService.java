package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.entity.MatchAdvantageTimelineEntity;
import com.dota2.dota2dataperser.entity.MatchEntity;
import com.dota2.dota2dataperser.entity.PickBanEntity;
import com.dota2.dota2dataperser.entity.PlayerMatchStatsEntity;
import com.dota2.dota2dataperser.repository.MatchAdvantageTimelineRepository;
import com.dota2.dota2dataperser.repository.MatchRepository;
import com.dota2.dota2dataperser.repository.PickBanRepository;
import com.dota2.dota2dataperser.repository.PlayerMatchStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchQueryService { //Для будущего сайта/API. Он только читает данные из БД.

    private final MatchRepository matchRepository;
    private final PlayerMatchStatsRepository playerMatchStatsRepository;
    private final PickBanRepository pickBanRepository;
    private final MatchAdvantageTimelineRepository timelineRepository;

    public MatchQueryService(
            MatchRepository matchRepository,
            PlayerMatchStatsRepository playerMatchStatsRepository,
            PickBanRepository pickBanRepository,
            MatchAdvantageTimelineRepository timelineRepository
    ) {
        this.matchRepository = matchRepository;
        this.playerMatchStatsRepository = playerMatchStatsRepository;
        this.pickBanRepository = pickBanRepository;
        this.timelineRepository = timelineRepository;
    }

    @Transactional(readOnly = true)
    public MatchEntity getMatch(Long matchId) {
        return matchRepository.findByMatchId(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));
    }

    @Transactional(readOnly = true)
    public List<MatchEntity> getMatchesByLeague(Long leagueId) {
        return matchRepository.findByLeagueId(leagueId);
    }

    @Transactional(readOnly = true)
    public List<MatchEntity> getMatchesByTeam(Long teamId) {
        return matchRepository.findByRadiantTeamIdOrDireTeamId(teamId, teamId);
    }

    @Transactional(readOnly = true)
    public List<PlayerMatchStatsEntity> getPlayersByMatch(Long matchId) {
        return playerMatchStatsRepository.findByMatchId(matchId);
    }

    @Transactional(readOnly = true)
    public List<PickBanEntity> getDraftByMatch(Long matchId) {
        return pickBanRepository.findByMatchIdOrderByOrderNumAsc(matchId);
    }

    @Transactional(readOnly = true)
    public List<MatchAdvantageTimelineEntity> getTimelineByMatch(Long matchId) {
        return timelineRepository.findByMatchIdOrderByMinuteAsc(matchId);
    }
}
