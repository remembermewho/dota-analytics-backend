package com.dota2.dota2dataperser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "match_advantage_timeline",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_match_advantage_timeline_match_minute",
                        columnNames = {"match_id", "minute"}
                )
        },
        indexes = {
                @Index(name = "idx_timeline_match_id", columnList = "match_id")
        }
)
public class MatchAdvantageTimelineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "minute", nullable = false)
    private Integer minute;

    @Column(name = "radiant_gold_adv")
    private Integer radiantGoldAdv;

    @Column(name = "radiant_xp_adv")
    private Integer radiantXpAdv;
}