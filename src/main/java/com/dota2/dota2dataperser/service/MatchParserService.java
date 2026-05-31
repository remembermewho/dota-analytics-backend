package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaHeroDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaLeagueDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaLeagueMatchDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.exception.ParsingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchParserService {

    private final ObjectMapper objectMapper;

    public MatchParserService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public OpenDotaMatchDto parseMatchJson(String json) {
        try {
            return objectMapper.readValue(json, OpenDotaMatchDto.class);
        } catch (Exception e) {
            throw new ParsingException("Failed to parse OpenDota match JSON", e);
        }
    }

    public List<OpenDotaLeagueDto> parseLeaguesJson(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<List<OpenDotaLeagueDto>>() {}
            );
        } catch (Exception e) {
            throw new ParsingException("Failed to parse OpenDota leagues JSON", e);
        }
    }

    public List<OpenDotaLeagueMatchDto> parseLeagueMatchesJson(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<List<OpenDotaLeagueMatchDto>>() {}
            );
        } catch (Exception e) {
            throw new ParsingException("Failed to parse OpenDota league matches JSON", e);
        }
    }

    public List<OpenDotaHeroDto> parseHeroesJson(String json) {
        try {
            return objectMapper.readValue(
                    json,
                    new TypeReference<List<OpenDotaHeroDto>>() {}
            );
        } catch (Exception e) {
            throw new ParsingException("Failed to parse OpenDota heroes JSON", e);
        }
    }
}
