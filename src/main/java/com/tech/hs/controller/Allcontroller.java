package com.tech.hs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tech.hs.dto.Pm10TrendRow;
import com.tech.hs.dto.Pm25TrendRow;
import com.tech.hs.service.TrendService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/hs/service")
@RequiredArgsConstructor
public class Allcontroller {

    private final TrendService trendService;

    @GetMapping("/index")
    public String index(Model model) {
        return "index"; 
    }

    @GetMapping("/trendchart/pm10")
    public String trendChart(@RequestParam String sido, Model model) {
        List<Pm10TrendRow> list = trendService.getPm10Trend(sido);
        model.addAttribute("pm10TrendList", list);
        model.addAttribute("sido", sido);
        return "pm10"; 
    }
    
    @GetMapping("/trendchart/pm25")
    public String pm25Chart(@RequestParam String sido, Model model) {
        List<Pm25TrendRow> list = trendService.getPm25Trend(sido);
        model.addAttribute("pm25TrendList", list);
        model.addAttribute("sido", sido);
        return "pm25"; 
    }
}
