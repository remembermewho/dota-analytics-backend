package com.dota2.dota2dataperser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    /**
     * Steam / Dota current nickname from match JSON.
     * This can be random, for example: "mode: m0NESY".
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * Stable professional nickname from OpenDota /proPlayers.
     * This is what we should show on the match page if available.
     */
    @Column(name = "pro_nickname")
    private String proNickname;

    @Column(name = "country")
    private String country;

    @Column(name = "pro_team_id")
    private Long proTeamId;

    @Column(name = "pro_team_name")
    private String proTeamName;

    @Column(name = "pro_team_tag")
    private String proTeamTag;

    @Column(name = "is_pro")
    private Boolean isPro;
}