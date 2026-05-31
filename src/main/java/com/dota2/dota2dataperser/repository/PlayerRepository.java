package com.dota2.dota2dataperser.repository;

import com.dota2.dota2dataperser.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    boolean existsByAccountId(Long accountId);

    Optional<PlayerEntity> findByAccountId(Long accountId);
}