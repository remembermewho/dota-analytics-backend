package com.dota2.dota2dataperser.dto.opendota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDotaPickBanDto {

    @JsonProperty("hero_id")
    private Integer heroId;

    /**
     * OpenDota:
     * 0 = Radiant
     * 1 = Dire
     */
    private Integer team;

    @JsonProperty("is_pick")
    private Boolean isPick;

    private Integer order;
}