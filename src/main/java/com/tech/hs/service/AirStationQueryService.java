package com.tech.hs.service;

import org.springframework.stereotype.Service;

import com.tech.hs.repository.AirStationRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirStationQueryService {

	private final AirStationRepository repo;

	@PostConstruct
	void checkIndex() {
		long count = repo.count();
		System.out.println("[BOOT] station index built (DB row count): " + count + "건");
	}

	public Coord findByStationName(String stationName) {
		return repo.findByStationName(stationName).map(e -> new Coord(e.getLat(), e.getLng())).orElse(null);

	}

	@lombok.Value
	public static class Coord {
		Double lat;
		Double lng;
	}
}
