package com.dota2.dota2dataperser.dto.opendota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDotaTeamDto {

    @JsonProperty("team_id")
    private Long teamId;

    private String name;

    private String tag;
}