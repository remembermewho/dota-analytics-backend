package com.dota2.dota2dataperser.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "team_roster",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_team_roster_team_player_joined",
                        columnNames = {"team_id", "account_id", "joined_at"}
                )
        },
        indexes = {
                @Index(name = "idx_team_roster_team_id", columnList = "team_id"),
                @Index(name = "idx_team_roster_account_id", columnList = "account_id")
        }
)
public class TeamRosterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;
}