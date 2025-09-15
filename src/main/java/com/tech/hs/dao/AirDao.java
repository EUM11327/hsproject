package com.tech.hs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tech.hs.dto.AirDto;
import com.tech.hs.dto.Pm10TrendRow;
import com.tech.hs.dto.Pm25TrendRow;

@Mapper
public interface AirDao {
	void insertAirQuality(AirDto dto);

	void insertAirQualityBatch(List<AirDto> list);

	List<Pm10TrendRow> selectPm10(String sido);

	void upsertAirQualityBatch(List<AirDto> list);
	
	// pm10 시간별 평균
	List<Pm10TrendRow> selectPm10Trend(@Param("sido") String sido,
            @Param("hours") int hours);
	// pm25 시간별 평균
	List<Pm25TrendRow> selectPm25Trend(@Param("sido") String sido,
            @Param("hours") int hours);
}
