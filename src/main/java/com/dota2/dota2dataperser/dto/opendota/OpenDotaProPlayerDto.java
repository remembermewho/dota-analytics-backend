package com.dota2.dota2dataperser.dto.opendota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDotaProPlayerDto {

    @JsonProperty("account_id")
    private Long accountId;

    /**
     * Professional nickname.
     */
    private String name;

    @JsonProperty("personaname")
    private String personaName;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("team_id")
    private Long teamId;

    @JsonProperty("team_name")
    private String teamName;

    @JsonProperty("team_tag")
    private String teamTag;

    @JsonProperty("is_pro")
    private Boolean isPro;
}