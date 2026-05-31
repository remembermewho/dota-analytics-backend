package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.entity.RawMatchEntity;
import com.dota2.dota2dataperser.repository.RawMatchRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RawMatchService {

    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_PARSED = "PARSED";
    public static final String STATUS_ERROR = "ERROR";

    private final RawMatchRepository rawMatchRepository;
    private final ObjectMapper objectMapper;

    public RawMatchService(
            RawMatchRepository rawMatchRepository,
            ObjectMapper objectMapper
    ) {
        this.rawMatchRepository = rawMatchRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRawMatch(Long matchId, String rawJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(rawJson);

            RawMatchEntity entity = rawMatchRepository
                    .findByMatchId(matchId)
                    .orElseGet(RawMatchEntity::new);

            entity.setMatchId(matchId);
            entity.setJsonData(jsonNode);
            entity.setDownloadedAt(LocalDateTime.now());
            entity.setParseStatus(STATUS_NEW);
            entity.setParseError(null);

            rawMatchRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save raw match JSON. matchId=" + matchId, e);
        }
    }

    @Transactional(readOnly = true)
    public boolean exists(Long matchId) {
        return rawMatchRepository.existsByMatchId(matchId);
    }

    @Transactional(readOnly = true)
    public boolean isParsed(Long matchId) {
        return rawMatchRepository.findByMatchId(matchId)
                .map(entity -> STATUS_PARSED.equals(entity.getParseStatus()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<String> getRawJson(Long matchId) {
        return rawMatchRepository.findByMatchId(matchId)
                .map(entity -> entity.getJsonData().toString());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markParsed(Long matchId) {
        RawMatchEntity entity = rawMatchRepository
                .findByMatchId(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Raw match not found: " + matchId));

        entity.setParseStatus(STATUS_PARSED);
        entity.setParsedAt(LocalDateTime.now());
        entity.setParseError(null);

        rawMatchRepository.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markError(Long matchId, String errorMessage) {
        RawMatchEntity entity = rawMatchRepository
                .findByMatchId(matchId)
                .orElseGet(RawMatchEntity::new);

        entity.setMatchId(matchId);

        if (entity.getDownloadedAt() == null) {
            entity.setDownloadedAt(LocalDateTime.now());
        }

        entity.setParseStatus(STATUS_ERROR);
        entity.setParseError(limitError(errorMessage));

        rawMatchRepository.save(entity);
    }

    private String limitError(String errorMessage) {
        if (errorMessage == null) {
            return null;
        }

        int maxLength = 3000;

        if (errorMessage.length() <= maxLength) {
            return errorMessage;
        }

        return errorMessage.substring(0, maxLength);
    }
}