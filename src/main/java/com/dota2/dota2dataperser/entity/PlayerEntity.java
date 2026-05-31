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

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "country")
    private String country;
}