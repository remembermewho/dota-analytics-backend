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
@Table(name = "heroes")
public class HeroEntity {

    @Id
    @Column(name = "hero_id")
    private Integer heroId;

    @Column(name = "name")
    private String name;

    @Column(name = "localized_name", nullable = false)
    private String localizedName;

    @Column(name = "primary_attr")
    private String primaryAttr;

    @Column(name = "attack_type")
    private String attackType;
}