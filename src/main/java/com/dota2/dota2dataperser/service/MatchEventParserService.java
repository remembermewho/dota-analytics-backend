package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.dto.opendota.OpenDotaPlayerDto;
import com.dota2.dota2dataperser.entity.MatchEventEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchEventParserService {

    public static final String FIRST_BLOOD = "FIRST_BLOOD";
    public static final String ROSHAN_KILL = "ROSHAN_KILL";
    public static final String TORMENTOR_KILL = "TORMENTOR_KILL";

    private final ObjectMapper objectMapper;

    public MatchEventParserService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<MatchEventEntity> parseEvents(OpenDotaMatchDto matchDto, String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);

            List<MatchEventEntity> events = new ArrayList<>();

            parseFirstBlood(matchDto, root, events);
            parseObjectives(matchDto, root, events);

            return events;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse match events. matchId=" + matchDto.getMatchId(), e);
        }
    }

    private void parseFirstBlood(
            OpenDotaMatchDto matchDto,
            JsonNode root,
            List<MatchEventEntity> events
    ) {
        Integer firstBloodTime = matchDto.getFirstBloodTime();

        if (firstBloodTime == null) {
            return;
        }

        JsonNode playersNode = root.get("players");

        if (playersNode != null && playersNode.isArray()) {
            for (JsonNode playerNode : playersNode) {
                int firstBloodClaimed = intValue(playerNode, "firstblood_claimed", 0);

                if (firstBloodClaimed <= 0) {
                    continue;
                }

                Long accountId = longValue(playerNode, "account_id");
                Integer heroId = intValueNullable(playerNode, "hero_id");
                Integer playerSlot = intValueNullable(playerNode, "player_slot");

                String teamSide = resolveTeamSideFromPlayerSlot(playerSlot);
                Long teamId = resolveTeamId(matchDto, teamSide);

                MatchEventEntity event = baseEvent(
                        matchDto.getMatchId(),
                        firstBloodTime,
                        FIRST_BLOOD
                );

                event.setTeamSide(teamSide);
                event.setTeamId(teamId);
                event.setAccountId(accountId);
                event.setHeroId(heroId);
                event.setPlayerSlot(playerSlot);
                event.setDescription("First Blood");
                event.setRawEvent(playerNode);

                events.add(event);
                return;
            }
        }

        MatchEventEntity fallback = baseEvent(
                matchDto.getMatchId(),
                firstBloodTime,
                FIRST_BLOOD
        );

        fallback.setDescription("First Blood");
        events.add(fallback);
    }

    private void parseObjectives(
            OpenDotaMatchDto matchDto,
            JsonNode root,
            List<MatchEventEntity> events
    ) {
        JsonNode objectivesNode = root.get("objectives");

        if (objectivesNode == null || !objectivesNode.isArray()) {
            return;
        }

        for (JsonNode objectiveNode : objectivesNode) {
            String rawType = textValue(objectiveNode, "type");

            if (rawType == null || rawType.isBlank()) {
                continue;
            }

            String eventType = resolveObjectiveEventType(rawType);

            if (eventType == null) {
                continue;
            }

            Integer eventTime = intValueNullable(objectiveNode, "time");

            MatchEventEntity event = baseEvent(
                    matchDto.getMatchId(),
                    eventTime,
                    eventType
            );

            String teamSide = resolveTeamSideFromObjective(objectiveNode);
            Long teamId = resolveTeamId(matchDto, teamSide);

            Integer playerSlot = resolvePlayerSlotFromObjective(objectiveNode);
            OpenDotaPlayerDto player = findPlayerByPlayerSlot(matchDto, playerSlot);

            event.setTeamSide(teamSide);
            event.setTeamId(teamId);
            event.setPlayerSlot(playerSlot);

            if (player != null) {
                event.setAccountId(player.getAccountId());
                event.setHeroId(player.getHeroId());
            }

            event.setDescription(buildDescription(eventType));
            event.setRawEvent(objectiveNode);

            events.add(event);
        }
    }

    private MatchEventEntity baseEvent(
            Long matchId,
            Integer eventTimeSeconds,
            String eventType
    ) {
        MatchEventEntity event = new MatchEventEntity();

        event.setMatchId(matchId);
        event.setEventTimeSeconds(eventTimeSeconds);
        event.setEventTimeMinSec(formatTime(eventTimeSeconds));
        event.setEventType(eventType);
        event.setCreatedAt(LocalDateTime.now());

        return event;
    }

    private String resolveObjectiveEventType(String rawType) {
        String type = rawType.toUpperCase();

        if (type.contains("ROSHAN") && type.contains("KILL")) {
            return ROSHAN_KILL;
        }

        if (type.contains("TORMENTOR") && type.contains("KILL")) {
            return TORMENTOR_KILL;
        }

        return null;
    }

    private String resolveTeamSideFromObjective(JsonNode objectiveNode) {
        Integer team = intValueNullable(objectiveNode, "team");

        if (team != null) {
            if (team == 0 || team == 2) {
                return "RADIANT";
            }

            if (team == 1 || team == 3) {
                return "DIRE";
            }
        }

        Integer playerSlot = resolvePlayerSlotFromObjective(objectiveNode);

        return resolveTeamSideFromPlayerSlot(playerSlot);
    }

    private Integer resolvePlayerSlotFromObjective(JsonNode objectiveNode) {
        Integer playerSlot = intValueNullable(objectiveNode, "player_slot");

        if (playerSlot != null) {
            return playerSlot;
        }

        Integer slot = intValueNullable(objectiveNode, "slot");

        if (slot == null) {
            return null;
        }

        if (slot >= 0 && slot <= 4) {
            return slot;
        }

        if (slot >= 5 && slot <= 9) {
            return 128 + (slot - 5);
        }

        return slot;
    }

    private OpenDotaPlayerDto findPlayerByPlayerSlot(
            OpenDotaMatchDto matchDto,
            Integer playerSlot
    ) {
        if (matchDto.getPlayers() == null || playerSlot == null) {
            return null;
        }

        return matchDto.getPlayers()
                .stream()
                .filter(player -> playerSlot.equals(player.getPlayerSlot()))
                .findFirst()
                .orElse(null);
    }

    private String resolveTeamSideFromPlayerSlot(Integer playerSlot) {
        if (playerSlot == null) {
            return "UNKNOWN";
        }

        return playerSlot < 128 ? "RADIANT" : "DIRE";
    }

    private Long resolveTeamId(OpenDotaMatchDto matchDto, String teamSide) {
        if ("RADIANT".equals(teamSide)) {
            return matchDto.getRadiantTeamId();
        }

        if ("DIRE".equals(teamSide)) {
            return matchDto.getDireTeamId();
        }

        return null;
    }

    private String buildDescription(String eventType) {
        return switch (eventType) {
            case FIRST_BLOOD -> "First Blood";
            case ROSHAN_KILL -> "Roshan killed";
            case TORMENTOR_KILL -> "Tormentor killed";
            default -> eventType;
        };
    }

    private String formatTime(Integer seconds) {
        if (seconds == null) {
            return null;
        }

        int minutes = seconds / 60;
        int secs = seconds % 60;

        return minutes + ":" + String.format("%02d", secs);
    }

    private String textValue(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);

        if (value == null || value.isNull()) {
            return null;
        }

        return value.asText();
    }

    private Long longValue(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);

        if (value == null || value.isNull()) {
            return null;
        }

        return value.asLong();
    }

    private Integer intValueNullable(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);

        if (value == null || value.isNull()) {
            return null;
        }

        return value.asInt();
    }

    private int intValue(JsonNode node, String fieldName, int defaultValue) {
        JsonNode value = node.get(fieldName);

        if (value == null || value.isNull()) {
            return defaultValue;
        }

        return value.asInt(defaultValue);
    }
}