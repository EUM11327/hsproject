package com.tech.hs.service;

import java.util.List;

import com.tech.hs.dto.Pm10TrendRow;
import com.tech.hs.dto.Pm25TrendRow;

public interface TrendService {
	
    List<Pm10TrendRow> getPm10Trend(String sido);
    
    List<Pm25TrendRow> getPm25Trend(String sido);
}
