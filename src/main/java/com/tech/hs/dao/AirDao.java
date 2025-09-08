package com.tech.hs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tech.hs.dto.AirDto;
import com.tech.hs.dto.Airpm10AvgDto;

@Mapper
public interface AirDao {
	void insertAirQuality(AirDto dto);

	void insertAirQualityBatch(List<AirDto> list);
	
	List<Airpm10AvgDto> selectPm10(String sido);
}
