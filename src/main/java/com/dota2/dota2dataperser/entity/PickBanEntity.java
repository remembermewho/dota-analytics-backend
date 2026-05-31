package com.dota2.dota2dataperser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "picks_bans",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_picks_bans_match_order",
                        columnNames = {"match_id", "order_num"}
                )
        },
        indexes = {
                @Index(name = "idx_pb_match_id", columnList = "match_id"),
                @Index(name = "idx_pb_hero_id", columnList = "hero_id"),
                @Index(name = "idx_pb_team_id", columnList = "team_id")
        }
)
public class PickBanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "hero_id", nullable = false)
    private Integer heroId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_side")
    private String teamSide;

    @Column(name = "is_pick", nullable = false)
    private Boolean isPick;

    @Column(name = "order_num", nullable = false)
    private Integer orderNum;
}