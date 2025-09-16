package com.tech.hs.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StationRealtimeDto {
    private String stationName;
    private Double lat;       
    private Double lng;       
    private Double value;     
    private String grade;     
    private String dataTime;  
}
