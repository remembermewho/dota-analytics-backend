package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.entity.PatchEntity;
import org.springframework.stereotype.Component;

@Component
public class PatchMapper {

    public PatchEntity toEntity(Integer patchId) {
        if (patchId == null) {
            return null;
        }

        PatchEntity entity = new PatchEntity();

        entity.setPatchId(patchId);
        entity.setPatchName("opendota_patch_" + patchId);

        return entity;
    }
}