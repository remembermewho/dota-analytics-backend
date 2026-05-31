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
@Table(name = "matches")
public class MatchEntity {

    @Id
    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "patch_id")
    private Integer patchId;

    @Column(name = "radiant_team_id")
    private Long radiantTeamId;

    @Column(name = "dire_team_id")
    private Long direTeamId;

    @Column(name = "radiant_win", nullable = false)
    private Boolean radiantWin;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "radiant_score")
    private Integer radiantScore;

    @Column(name = "dire_score")
    private Integer direScore;

    @Column(name = "first_blood_time")
    private Integer firstBloodTime;

    @Column(name = "cluster")
    private Integer cluster;

    @Column(name = "game_mode")
    private Integer gameMode;

    @Column(name = "lobby_type")
    private Integer lobbyType;

    @Column(name = "match_seq_num")
    private Long matchSeqNum;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}