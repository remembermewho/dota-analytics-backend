package com.dota2.dota2dataperser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "patches")
public class PatchEntity {

    @Id
    @Column(name = "patch_id")
    private Integer patchId;

    @Column(name = "patch_name", nullable = false)
    private String patchName;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;
}