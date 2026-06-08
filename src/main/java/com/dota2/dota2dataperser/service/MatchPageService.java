package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.dto.response.*;
import com.dota2.dota2dataperser.entity.*;
import com.dota2.dota2dataperser.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchPageService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final HeroRepository heroRepository;
    private final PlayerMatchStatsRepository playerMatchStatsRepository;
    private final PickBanRepository pickBanRepository;
    private final MatchAdvantageTimelineRepository timelineRepository;
    private final MatchEventRepository matchEventRepository;

    public MatchPageService(
            MatchRepository matchRepository,
            TournamentRepository tournamentRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            HeroRepository heroRepository,
            PlayerMatchStatsRepository playerMatchStatsRepository,
            PickBanRepository pickBanRepository,
            MatchAdvantageTimelineRepository timelineRepository, MatchEventRepository matchEventRepository
    ) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.heroRepository = heroRepository;
        this.playerMatchStatsRepository = playerMatchStatsRepository;
        this.pickBanRepository = pickBanRepository;
        this.timelineRepository = timelineRepository;
        this.matchEventRepository = matchEventRepository;
    }

    @Transactional(readOnly = true)
    public MatchFullResponse getFullMatch(Long matchId) {
        MatchEntity match = matchRepository.findByMatchId(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        String tournamentName = resolveTournamentName(match.getLeagueId());

        TeamEntity radiantTeam = findTeam(match.getRadiantTeamId());
        TeamEntity direTeam = findTeam(match.getDireTeamId());

        MatchTeamResponse radiant = new MatchTeamResponse(
                match.getRadiantTeamId(),
                radiantTeam != null ? radiantTeam.getName() : "Unknown Radiant",
                match.getRadiantScore(),
                Boolean.TRUE.equals(match.getRadiantWin())
        );

        MatchTeamResponse dire = new MatchTeamResponse(
                match.getDireTeamId(),
                direTeam != null ? direTeam.getName() : "Unknown Dire",
                match.getDireScore(),
                Boolean.FALSE.equals(match.getRadiantWin())
        );

        List<MatchEventResponse> events =
                matchEventRepository.findByMatchIdOrderByEventTimeSecondsAsc(matchId)
                        .stream()
                        .map(this::toEventResponse)
                        .toList();

        List<PickBanEntity> draftEntries =
                pickBanRepository.findByMatchIdOrderByOrderNumAsc(matchId);

        List<DraftEntryResponse> picks = draftEntries.stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getIsPick()))
                .map(this::toDraftResponse)
                .toList();

        List<DraftEntryResponse> bans = draftEntries.stream()
                .filter(entry -> Boolean.FALSE.equals(entry.getIsPick()))
                .map(this::toDraftResponse)
                .toList();

        List<MatchPlayerResponse> players = playerMatchStatsRepository.findByMatchId(matchId)
                .stream()
                .map(this::toPlayerResponse)
                .toList();

        List<MatchTimelinePointResponse> timeline =
                timelineRepository.findByMatchIdOrderByMinuteAsc(matchId)
                        .stream()
                        .map(this::toTimelineResponse)
                        .toList();

        Integer totalKills = safeInt(match.getRadiantScore()) + safeInt(match.getDireScore());

        return new MatchFullResponse(
                match.getMatchId(),
                tournamentName,
                match.getStartTime() != null ? match.getStartTime().toString() : null,
                match.getDuration(),
                formatDuration(match.getDuration()),

                radiant,
                dire,

                totalKills,

                picks,
                bans,

                players,

                timeline,

                events
        );
    }

    private DraftEntryResponse toDraftResponse(PickBanEntity entity) {
        TeamEntity team = findTeam(entity.getTeamId());
        HeroEntity hero = findHero(entity.getHeroId());

        return new DraftEntryResponse(
                entity.getOrderNum(),
                entity.getTeamSide(),
                entity.getTeamId(),
                team != null ? team.getName() : null,
                entity.getHeroId(),
                hero != null ? hero.getLocalizedName() : "Unknown hero " + entity.getHeroId(),
                entity.getIsPick()
        );
    }

    private MatchEventResponse toEventResponse(MatchEventEntity entity) {
        TeamEntity team = findTeam(entity.getTeamId());
        PlayerEntity player = findPlayer(entity.getAccountId());
        HeroEntity hero = findHero(entity.getHeroId());

        String playerName = null;

        if (player != null) {
            playerName = resolveDisplayName(
                    player.getAccountId(),
                    player.getProNickname(),
                    player.getNickname()
            );
        }

        return new MatchEventResponse(
                entity.getEventTimeSeconds(),
                entity.getEventTimeMinSec(),
                entity.getEventType(),
                entity.getTeamSide(),
                entity.getTeamId(),
                team != null ? team.getName() : null,
                entity.getAccountId(),
                playerName,
                entity.getHeroId(),
                hero != null ? hero.getLocalizedName() : null,
                entity.getDescription()
        );
    }

    private MatchPlayerResponse toPlayerResponse(PlayerMatchStatsEntity entity) {
        PlayerEntity player = findPlayer(entity.getAccountId());
        TeamEntity team = findTeam(entity.getTeamId());
        HeroEntity hero = findHero(entity.getHeroId());

        String teamSide = resolveTeamSide(entity.getIsRadiant());

        String nickname = player != null ? player.getNickname() : null;
        String proNickname = player != null ? player.getProNickname() : null;

        String displayName = resolveDisplayName(
                entity.getAccountId(),
                proNickname,
                nickname
        );

        return new MatchPlayerResponse(
                entity.getAccountId(),

                displayName,
                nickname,
                proNickname,

                entity.getTeamId(),
                team != null ? team.getName() : null,
                teamSide,

                entity.getHeroId(),
                hero != null ? hero.getLocalizedName() : "Unknown hero " + entity.getHeroId(),

                entity.getKills(),
                entity.getDeaths(),
                entity.getAssists(),

                entity.getGpm(),
                entity.getXpm(),
                entity.getNetWorth(),

                entity.getHeroDamage(),
                entity.getTowerDamage(),
                entity.getHeroHealing(),

                entity.getLastHits(),
                entity.getDenies(),
                entity.getLevel()
        );
    }

    private String resolveDisplayName(
            Long accountId,
            String proNickname,
            String nickname
    ) {
        if (proNickname != null && !proNickname.isBlank()) {
            return proNickname;
        }

        if (nickname != null && !nickname.isBlank()) {
            return nickname;
        }

        return "Unknown player " + accountId;
    }

    private MatchTimelinePointResponse toTimelineResponse(MatchAdvantageTimelineEntity entity) {
        return new MatchTimelinePointResponse(
                entity.getMinute(),
                entity.getRadiantGoldAdv(),
                entity.getRadiantXpAdv()
        );
    }

    private String resolveTournamentName(Long leagueId) {
        if (leagueId == null) {
            return null;
        }

        return tournamentRepository.findById(leagueId)
                .map(TournamentEntity::getName)
                .orElse("Unknown tournament " + leagueId);
    }

    private TeamEntity findTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }

        return teamRepository.findByTeamId(teamId)
                .orElse(null);
    }

    private PlayerEntity findPlayer(Long accountId) {
        if (accountId == null) {
            return null;
        }

        return playerRepository.findByAccountId(accountId)
                .orElse(null);
    }

    private HeroEntity findHero(Integer heroId) {
        if (heroId == null) {
            return null;
        }

        return heroRepository.findByHeroId(heroId)
                .orElse(null);
    }

    private String resolveTeamSide(Boolean isRadiant) {
        if (isRadiant == null) {
            return "UNKNOWN";
        }

        return isRadiant ? "RADIANT" : "DIRE";
    }

    private String formatDuration(Integer durationSeconds) {
        if (durationSeconds == null) {
            return null;
        }

        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;

        return minutes + ":" + String.format("%02d", seconds);
    }

    private int safeInt(Integer value) {
        return value != null ? value : 0;
    }
}