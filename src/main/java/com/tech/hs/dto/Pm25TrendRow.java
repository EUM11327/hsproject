package com.tech.hs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pm25TrendRow {

	private String dataTime;
	private Integer pm25Avg;
}
