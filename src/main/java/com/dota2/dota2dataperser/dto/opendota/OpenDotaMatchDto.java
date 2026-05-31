package com.dota2.dota2dataperser.dto.opendota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenDotaMatchDto {

    @JsonProperty("match_id")
    private Long matchId;

    @JsonProperty("match_seq_num")
    private Long matchSeqNum;

    @JsonProperty("leagueid")
    private Long leagueId;

    private Integer patch;

    @JsonProperty("radiant_team_id")
    private Long radiantTeamId;

    @JsonProperty("dire_team_id")
    private Long direTeamId;

    @JsonProperty("radiant_win")
    private Boolean radiantWin;

    private Integer duration;

    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("radiant_score")
    private Integer radiantScore;

    @JsonProperty("dire_score")
    private Integer direScore;

    @JsonProperty("first_blood_time")
    private Integer firstBloodTime;

    private Integer cluster;

    @JsonProperty("game_mode")
    private Integer gameMode;

    @JsonProperty("lobby_type")
    private Integer lobbyType;

    @JsonProperty("radiant_team")
    private OpenDotaTeamDto radiantTeam;

    @JsonProperty("dire_team")
    private OpenDotaTeamDto direTeam;

    private List<OpenDotaPlayerDto> players;

    @JsonProperty("picks_bans")
    private List<OpenDotaPickBanDto> picksBans;

    @JsonProperty("radiant_gold_adv")
    private List<Integer> radiantGoldAdv;

    @JsonProperty("radiant_xp_adv")
    private List<Integer> radiantXpAdv;
}