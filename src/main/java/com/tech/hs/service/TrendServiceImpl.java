package com.tech.hs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tech.hs.dto.Pm10TrendRow;
import com.tech.hs.dto.Pm25TrendRow;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrendServiceImpl implements TrendService {

    private final AirService airService; 

    @Override
    public List<Pm10TrendRow> getPm10Trend(String sido) {
        return airService.fetchPm10Trend(sido); 
    }
    
    @Override
    public List<Pm25TrendRow> getPm25Trend(String sido) {
        return airService.fetchPm25Trend(sido);
    }
}
