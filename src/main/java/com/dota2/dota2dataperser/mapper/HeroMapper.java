package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaHeroDto;
import com.dota2.dota2dataperser.entity.HeroEntity;
import org.springframework.stereotype.Component;

@Component
public class HeroMapper {

    public HeroEntity toEntity(OpenDotaHeroDto dto) {
        HeroEntity entity = new HeroEntity();

        entity.setHeroId(dto.getId());
        entity.setName(dto.getName());
        entity.setLocalizedName(dto.getLocalizedName());
        entity.setPrimaryAttr(dto.getPrimaryAttr());
        entity.setAttackType(dto.getAttackType());

        return entity;
    }
}