package com.tech.hs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pm10TrendRow {
	private String dataTime; //측정 시간
	private Integer pm10Avg; // 측정 평균
}
