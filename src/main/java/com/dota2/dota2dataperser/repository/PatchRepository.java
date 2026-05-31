package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.PatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatchRepository extends JpaRepository<PatchEntity, Integer> {

    boolean existsByPatchId(Integer patchId);
}