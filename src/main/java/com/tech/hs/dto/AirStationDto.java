package com.tech.hs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirStationDto {
	private Long stationId;
	private String stationName;
	private String addr;
	private String mangName;
	private Double lat;
	private Double lng;
	private Integer startedYear;
	private String itemsText;
}
