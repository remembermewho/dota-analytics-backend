package com.dota2.dota2dataperser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tournaments")
public class TournamentEntity {

    @Id
    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tier")
    private String tier;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;
}