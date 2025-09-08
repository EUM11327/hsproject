package com.tech.hs.runner;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.tech.hs.service.AirService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AirBootRunner implements ApplicationRunner {

	private final AirService airService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (String sido : List.of("서울", "경기", "부산", "대전")) {
			airService.fetchAndSaveAirQuality(sido);
		}
	}
}
