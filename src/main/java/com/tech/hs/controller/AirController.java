package com.tech.hs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tech.hs.service.AirService;

@Controller
@RequestMapping("/hs/api")
public class AirController {

	private final AirService airService;

	public AirController(AirService airService) {
		this.airService = airService;
	}

	@GetMapping("/fetch-now")
	public String fetchNow() throws Exception {
		for (String sido : java.util.List.of("서울", "경기", "부산", "대전")) {
			airService.fetchAndSaveAirQuality(sido);
		}
		return "ok";
	}
}
