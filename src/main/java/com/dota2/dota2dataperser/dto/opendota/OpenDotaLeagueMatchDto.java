package com.dota2.dota2dataperser.dto.opendota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDotaLeagueMatchDto {

    @JsonProperty("match_id")
    private Long matchId;

    @JsonProperty("match_seq_num")
    private Long matchSeqNum;

    @JsonProperty("radiant_win")
    private Boolean radiantWin;

    private Integer duration;

    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("radiant_team_id")
    private Long radiantTeamId;

    @JsonProperty("dire_team_id")
    private Long direTeamId;

    @JsonProperty("radiant_name")
    private String radiantName;

    @JsonProperty("dire_name")
    private String direName;

    @JsonProperty("leagueid")
    private Long leagueId;
}
