package com.dota2.dota2dataperser.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "match_events",
        indexes = {
                @Index(name = "idx_match_events_match_id", columnList = "match_id"),
                @Index(name = "idx_match_events_event_type", columnList = "event_type"),
                @Index(name = "idx_match_events_team_id", columnList = "team_id"),
                @Index(name = "idx_match_events_account_id", columnList = "account_id"),
                @Index(name = "idx_match_events_hero_id", columnList = "hero_id")
        }
)
public class MatchEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "event_time_seconds")
    private Integer eventTimeSeconds;

    @Column(name = "event_time_min_sec")
    private String eventTimeMinSec;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "team_side")
    private String teamSide;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "hero_id")
    private Integer heroId;

    @Column(name = "player_slot")
    private Integer playerSlot;

    @Column(name = "description")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_event", columnDefinition = "jsonb")
    private JsonNode rawEvent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}