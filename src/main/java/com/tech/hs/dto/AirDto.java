package com.tech.hs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirDto {

	private String sidoName; // 시도명
	private String stationName; // 측정소명
	private String dataTime; // 측정 일시
	private String pm10Value; // 미세먼지 농도 
	private String pm25Value; // 초 미세먼지 농도
	private String khaiValue; // 통합 대기환경 수치
}
