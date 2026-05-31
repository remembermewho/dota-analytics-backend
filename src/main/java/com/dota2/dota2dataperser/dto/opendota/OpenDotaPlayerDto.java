package com.dota2.dota2dataperser.dto.opendota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDotaPlayerDto {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("personaname")
    private String nickname;

    @JsonProperty("hero_id")
    private Integer heroId;

    @JsonProperty("player_slot")
    private Integer playerSlot;

    @JsonProperty("isRadiant")
    private Boolean isRadiant;

    private Integer kills;

    private Integer deaths;

    private Integer assists;

    @JsonProperty("gold_per_min")
    private Integer goldPerMin;

    @JsonProperty("xp_per_min")
    private Integer xpPerMin;

    @JsonProperty("net_worth")
    private Integer netWorth;

    @JsonProperty("hero_damage")
    private Integer heroDamage;

    @JsonProperty("tower_damage")
    private Integer towerDamage;

    @JsonProperty("hero_healing")
    private Integer heroHealing;

    @JsonProperty("last_hits")
    private Integer lastHits;

    private Integer denies;

    private Integer level;

    @JsonProperty("obs_placed")
    private Integer obsPlaced;

    @JsonProperty("sen_placed")
    private Integer senPlaced;
}