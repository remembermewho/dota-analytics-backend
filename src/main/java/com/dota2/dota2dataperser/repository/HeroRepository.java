package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.HeroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeroRepository extends JpaRepository<HeroEntity, Integer> {

    boolean existsByHeroId(Integer heroId);

    Optional<HeroEntity> findByHeroId(Integer heroId);
}