package com.tech.hs.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tech.hs.dto.AirRealtimeItem;
import com.tech.hs.dto.StationRealtimeDto;
import com.tech.hs.service.AirService;
import com.tech.hs.service.AirStationQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hs/air")
public class AirMapController {

	private final AirService airService; // 실시간 값 호출
	private final AirStationQueryService stationQuery; // DB/캐시에서 측정소 좌표 조회

	@GetMapping("/realtime")
	public List<StationRealtimeDto> listRealtime(@RequestParam String sido,
			@RequestParam(defaultValue = "PM10") String metric) throws Exception {

		List<AirRealtimeItem> realtime = airService.getSidoRealtime(sido);

		// === 디버그 카운터 추가 ===
		var miss = new java.util.concurrent.atomic.AtomicInteger();
		var dash = new java.util.concurrent.atomic.AtomicInteger();

		List<StationRealtimeDto> out = realtime.stream().map(it -> {
			var coord = stationQuery.findByStationName(it.getStationName());
			boolean hasDash = "-".equals(it.getPm10Value()) || "-".equals(it.getPm25Value());
			if (coord == null)
				miss.incrementAndGet();
			if (hasDash)
				dash.incrementAndGet();
			if (coord == null || hasDash)
				return null;

			return new StationRealtimeDto(it.getStationName(), coord.getLat(), coord.getLng(),
					it.getValueByMetric(metric), 
					null, it.getDataTime());
		}).filter(java.util.Objects::nonNull).toList();

		System.out.printf("[MAP] total=%d, noCoord=%d, dashValue=%d, out=%d%n", realtime.size(), miss.get(), dash.get(),
				out.size());

		return out;
	}

}
