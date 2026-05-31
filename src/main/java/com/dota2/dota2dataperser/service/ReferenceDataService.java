package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaHeroDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaLeagueDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaPlayerDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaTeamDto;
import com.dota2.dota2dataperser.entity.*;
import com.dota2.dota2dataperser.mapper.HeroMapper;
import com.dota2.dota2dataperser.mapper.PatchMapper;
import com.dota2.dota2dataperser.mapper.TeamMapper;
import com.dota2.dota2dataperser.mapper.TournamentMapper;
import com.dota2.dota2dataperser.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReferenceDataService {

    private final TournamentRepository tournamentRepository;
    private final PatchRepository patchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final HeroRepository heroRepository;

    private final TournamentMapper tournamentMapper;
    private final PatchMapper patchMapper;
    private final TeamMapper teamMapper;
    private final HeroMapper heroMapper;

    public ReferenceDataService(
            TournamentRepository tournamentRepository,
            PatchRepository patchRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            HeroRepository heroRepository,
            TournamentMapper tournamentMapper,
            PatchMapper patchMapper,
            TeamMapper teamMapper,
            HeroMapper heroMapper
    ) {
        this.tournamentRepository = tournamentRepository;
        this.patchRepository = patchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.heroRepository = heroRepository;
        this.tournamentMapper = tournamentMapper;
        this.patchMapper = patchMapper;
        this.teamMapper = teamMapper;
        this.heroMapper = heroMapper;
    }

    @Transactional
    public void saveTournament(OpenDotaLeagueDto dto) {
        if (dto == null || dto.getLeagueId() == null) {
            return;
        }

        TournamentEntity entity = tournamentRepository
                .findById(dto.getLeagueId())
                .orElseGet(() -> tournamentMapper.toEntity(dto));

        entity.setName(dto.getName() != null ? dto.getName() : entity.getName());
        entity.setTier(dto.getTier() != null ? dto.getTier() : entity.getTier());

        tournamentRepository.save(entity);
    }

    @Transactional
    public void saveTournaments(List<OpenDotaLeagueDto> leagues) {
        if (leagues == null) {
            return;
        }

        for (OpenDotaLeagueDto league : leagues) {
            saveTournament(league);
        }
    }

    @Transactional
    public void savePatch(Integer patchId) {
        if (patchId == null) {
            return;
        }

        if (!patchRepository.existsByPatchId(patchId)) {
            PatchEntity entity = patchMapper.toEntity(patchId);
            patchRepository.save(entity);
        }
    }

    @Transactional
    public void saveTeamsFromMatch(OpenDotaMatchDto dto) {
        saveTeamFromDto(dto.getRadiantTeam(), dto.getRadiantTeamId());
        saveTeamFromDto(dto.getDireTeam(), dto.getDireTeamId());
    }

    @Transactional
    public void savePlayersFromMatch(OpenDotaMatchDto dto) {
        if (dto.getPlayers() == null) {
            return;
        }

        for (OpenDotaPlayerDto playerDto : dto.getPlayers()) {
            if (playerDto == null || playerDto.getAccountId() == null) {
                continue;
            }

            PlayerEntity entity = playerRepository
                    .findByAccountId(playerDto.getAccountId())
                    .orElseGet(PlayerEntity::new);

            entity.setAccountId(playerDto.getAccountId());

            if (playerDto.getNickname() != null && !playerDto.getNickname().isBlank()) {
                entity.setNickname(playerDto.getNickname());
            }

            playerRepository.save(entity);
        }
    }

    @Transactional
    public void saveHeroes(List<OpenDotaHeroDto> heroes) {
        if (heroes == null) {
            return;
        }

        for (OpenDotaHeroDto heroDto : heroes) {
            if (heroDto == null || heroDto.getId() == null) {
                continue;
            }

            HeroEntity incoming = heroMapper.toEntity(heroDto);

            HeroEntity entity = heroRepository
                    .findByHeroId(heroDto.getId())
                    .orElseGet(HeroEntity::new);

            entity.setHeroId(incoming.getHeroId());
            entity.setName(incoming.getName());
            entity.setLocalizedName(incoming.getLocalizedName());
            entity.setPrimaryAttr(incoming.getPrimaryAttr());
            entity.setAttackType(incoming.getAttackType());

            heroRepository.save(entity);
        }
    }

    @Transactional
    public void saveUnknownHeroesFromMatch(OpenDotaMatchDto dto) {
        if (dto.getPlayers() != null) {
            for (OpenDotaPlayerDto player : dto.getPlayers()) {
                if (player != null) {
                    saveUnknownHeroIfMissing(player.getHeroId());
                }
            }
        }

        if (dto.getPicksBans() != null) {
            dto.getPicksBans().forEach(pb -> {
                if (pb != null) {
                    saveUnknownHeroIfMissing(pb.getHeroId());
                }
            });
        }
    }

    @Transactional
    public void saveUnknownTournamentIfMissing(Long leagueId) {
        if (leagueId == null) {
            return;
        }

        if (!tournamentRepository.existsByLeagueId(leagueId)) {
            TournamentEntity entity = new TournamentEntity();

            entity.setLeagueId(leagueId);
            entity.setName("Unknown tournament " + leagueId);

            tournamentRepository.save(entity);
        }
    }

    @Transactional
    public void saveReferencesFromMatch(OpenDotaMatchDto dto) {
        if (dto == null) {
            return;
        }

        saveUnknownTournamentIfMissing(dto.getLeagueId());
        savePatch(dto.getPatch());
        saveTeamsFromMatch(dto);
        savePlayersFromMatch(dto);
        saveUnknownHeroesFromMatch(dto);
    }

    private void saveTeamFromDto(OpenDotaTeamDto teamDto, Long fallbackTeamId) {
        Long teamId = teamDto != null && teamDto.getTeamId() != null
                ? teamDto.getTeamId()
                : fallbackTeamId;

        if (teamId == null) {
            return;
        }

        TeamEntity entity = teamRepository
                .findByTeamId(teamId)
                .orElseGet(TeamEntity::new);

        entity.setTeamId(teamId);

        if (teamDto != null) {
            if (teamDto.getName() != null && !teamDto.getName().isBlank()) {
                entity.setName(teamDto.getName());
            }

            if (teamDto.getTag() != null && !teamDto.getTag().isBlank()) {
                entity.setTag(teamDto.getTag());
            }
        }

        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName("Unknown team " + teamId);
        }

        teamRepository.save(entity);
    }

    private void saveUnknownHeroIfMissing(Integer heroId) {
        if (heroId == null) {
            return;
        }

        if (!heroRepository.existsByHeroId(heroId)) {
            HeroEntity entity = new HeroEntity();

            entity.setHeroId(heroId);
            entity.setLocalizedName("Unknown hero " + heroId);

            heroRepository.save(entity);
        }
    }
}
