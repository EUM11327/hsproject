package com.tech.hs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tech.hs.entity.AirStationEntity;

public interface AirStationRepository extends JpaRepository<AirStationEntity, Long> {
    Optional<AirStationEntity> findByStationName(String stationName);
}
