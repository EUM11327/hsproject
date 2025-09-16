package com.tech.hs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tech.hs.dto.AirStationDto;
import com.tech.hs.entity.AirStationEntity;


@Mapper
public interface AirStationDao {
	int upsert(AirStationDto s);

	AirStationDto findByName(@Param("name") String name);
	
	AirStationEntity findByStationName(@Param("name") String name);

	java.util.List<AirStationDto> findAll();
}
