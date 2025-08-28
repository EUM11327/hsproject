package com.tech.hs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.hs.service.AirService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hs/service")
public class AirController {

	private final AirService airService;

	 @GetMapping("/{sido}")
	    public String read(@PathVariable String sido) {
	        return airService.getAirData(sido);
	    }
}
