package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaPickBanDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaPlayerDto;
import com.dota2.dota2dataperser.entity.MatchAdvantageTimelineEntity;
import com.dota2.dota2dataperser.entity.PickBanEntity;
import com.dota2.dota2dataperser.entity.PlayerMatchStatsEntity;
import com.dota2.dota2dataperser.mapper.MatchMapper;
import com.dota2.dota2dataperser.mapper.PickBanMapper;
import com.dota2.dota2dataperser.mapper.PlayerMapper;
import com.dota2.dota2dataperser.mapper.TimelineMapper;
import com.dota2.dota2dataperser.repository.MatchAdvantageTimelineRepository;
import com.dota2.dota2dataperser.repository.MatchRepository;
import com.dota2.dota2dataperser.repository.PickBanRepository;
import com.dota2.dota2dataperser.repository.PlayerMatchStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NormalizedMatchSaveService {

    private final ReferenceDataService referenceDataService;

    private final MatchRepository matchRepository;
    private final PlayerMatchStatsRepository playerMatchStatsRepository;
    private final PickBanRepository pickBanRepository;
    private final MatchAdvantageTimelineRepository timelineRepository;

    private final MatchMapper matchMapper;
    private final PlayerMapper playerMapper;
    private final PickBanMapper pickBanMapper;
    private final TimelineMapper timelineMapper;

    public NormalizedMatchSaveService(
            ReferenceDataService referenceDataService,
            MatchRepository matchRepository,
            PlayerMatchStatsRepository playerMatchStatsRepository,
            PickBanRepository pickBanRepository,
            MatchAdvantageTimelineRepository timelineRepository,
            MatchMapper matchMapper,
            PlayerMapper playerMapper,
            PickBanMapper pickBanMapper,
            TimelineMapper timelineMapper
    ) {
        this.referenceDataService = referenceDataService;
        this.matchRepository = matchRepository;
        this.playerMatchStatsRepository = playerMatchStatsRepository;
        this.pickBanRepository = pickBanRepository;
        this.timelineRepository = timelineRepository;
        this.matchMapper = matchMapper;
        this.playerMapper = playerMapper;
        this.pickBanMapper = pickBanMapper;
        this.timelineMapper = timelineMapper;
    }

    @Transactional
    public void saveNormalizedMatch(OpenDotaMatchDto dto) {
        validate(dto);

        Long matchId = dto.getMatchId();

        referenceDataService.saveReferencesFromMatch(dto);

        matchRepository.save(matchMapper.toEntity(dto));

        playerMatchStatsRepository.deleteByMatchId(matchId);
        pickBanRepository.deleteByMatchId(matchId);
        timelineRepository.deleteByMatchId(matchId);

        savePlayerMatchStats(dto);
        savePicksBans(dto);
        saveTimeline(dto);
    }

    private void savePlayerMatchStats(OpenDotaMatchDto dto) {
        if (dto.getPlayers() == null || dto.getPlayers().isEmpty()) {
            return;
        }

        List<PlayerMatchStatsEntity> entities = new ArrayList<>();

        for (OpenDotaPlayerDto playerDto : dto.getPlayers()) {
            if (playerDto == null) {
                continue;
            }

            if (playerDto.getAccountId() == null) {
                continue;
            }

            if (playerDto.getHeroId() == null) {
                continue;
            }

            PlayerMatchStatsEntity entity = playerMapper.toStatsEntity(playerDto, dto);

            if (entity != null) {
                entities.add(entity);
            }
        }

        playerMatchStatsRepository.saveAll(entities);
    }

    private void savePicksBans(OpenDotaMatchDto dto) {
        if (dto.getPicksBans() == null || dto.getPicksBans().isEmpty()) {
            return;
        }

        List<PickBanEntity> entities = new ArrayList<>();

        for (OpenDotaPickBanDto pickBanDto : dto.getPicksBans()) {
            if (pickBanDto == null) {
                continue;
            }

            if (pickBanDto.getHeroId() == null) {
                continue;
            }

            if (pickBanDto.getOrder() == null) {
                continue;
            }

            PickBanEntity entity = pickBanMapper.toEntity(pickBanDto, dto);
            entities.add(entity);
        }

        pickBanRepository.saveAll(entities);
    }

    private void saveTimeline(OpenDotaMatchDto dto) {
        List<MatchAdvantageTimelineEntity> timeline = timelineMapper.toEntities(dto);

        if (timeline.isEmpty()) {
            return;
        }

        timelineRepository.saveAll(timeline);
    }

    private void validate(OpenDotaMatchDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OpenDotaMatchDto is null");
        }

        if (dto.getMatchId() == null) {
            throw new IllegalArgumentException("match_id is null");
        }

        if (dto.getRadiantWin() == null) {
            throw new IllegalArgumentException("radiant_win is null. matchId=" + dto.getMatchId());
        }

        if (dto.getDuration() == null) {
            throw new IllegalArgumentException("duration is null. matchId=" + dto.getMatchId());
        }
    }
}
