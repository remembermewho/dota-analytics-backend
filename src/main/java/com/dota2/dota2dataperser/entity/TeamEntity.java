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
@Table(name = "teams")
public class TeamEntity {

    @Id
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tag")
    private String tag;

    @Column(name = "region")
    private String region;

    @Column(name = "logo_url")
    private String logoUrl;
}