package com.tech.hs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AirRealtimeItem {
    private String stationName;
    private String dataTime;
    private String pm10Value;
    private String pm10Grade;
    private String pm25Value;
    private String pm25Grade;
    
    public Double getValueByMetric(String metric) {
        return switch (metric.toUpperCase()) {
            case "PM10" -> Double.valueOf(this.getPm10Value());
            case "PM25" -> Double.valueOf(this.getPm25Value());
            default     -> null;
        };
    }

}
