package com.dota2.dota2dataperser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "player_match_stats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_player_match_stats_match_account",
                        columnNames = {"match_id", "account_id"}
                )
        },
        indexes = {
                @Index(name = "idx_pms_match_id", columnList = "match_id"),
                @Index(name = "idx_pms_account_id", columnList = "account_id"),
                @Index(name = "idx_pms_hero_id", columnList = "hero_id"),
                @Index(name = "idx_pms_team_id", columnList = "team_id")
        }
)
public class PlayerMatchStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "hero_id", nullable = false)
    private Integer heroId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "player_slot")
    private Integer playerSlot;

    @Column(name = "is_radiant")
    private Boolean isRadiant;

    @Column(name = "kills")
    private Integer kills;

    @Column(name = "deaths")
    private Integer deaths;

    @Column(name = "assists")
    private Integer assists;

    @Column(name = "gpm")
    private Integer gpm;

    @Column(name = "xpm")
    private Integer xpm;

    @Column(name = "net_worth")
    private Integer netWorth;

    @Column(name = "hero_damage")
    private Integer heroDamage;

    @Column(name = "tower_damage")
    private Integer towerDamage;

    @Column(name = "hero_healing")
    private Integer heroHealing;

    @Column(name = "last_hits")
    private Integer lastHits;

    @Column(name = "denies")
    private Integer denies;

    @Column(name = "level")
    private Integer level;

    @Column(name = "obs_placed")
    private Integer obsPlaced;

    @Column(name = "sen_placed")
    private Integer senPlaced;
}