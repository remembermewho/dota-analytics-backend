package com.dota2.dota2dataperser.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "raw_matches")
public class RawMatchEntity {

    @Id
    @Column(name = "match_id")
    private Long matchId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "json_data", columnDefinition = "jsonb", nullable = false)
    private JsonNode jsonData;

    @Column(name = "downloaded_at", nullable = false)
    private LocalDateTime downloadedAt;

    @Column(name = "parsed_at")
    private LocalDateTime parsedAt;

    @Column(name = "parse_status")
    private String parseStatus;

    @Column(name = "parse_error")
    private String parseError;
}