package com.tech.hs.service;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.hs.dao.AirDao;
import com.tech.hs.dto.AirDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirService {

	private final AirDao airDao;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private static final String API_BASE = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

	private static final String SERVICE_KEY = "5a9f2ede48b1ba5dede90f126eae8a9469568790595cd42b880b61c80480eddd";

	private static final DateTimeFormatter API_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Transactional
	public void fetchAndSaveAirQuality(String sido) throws Exception {

		URI url = UriComponentsBuilder.fromHttpUrl(API_BASE)
				.queryParam("serviceKey", SERVICE_KEY)
				.queryParam("returnType", "json")
				.queryParam("numOfRows", 100)
				.queryParam("pageNo", 1)
				.queryParam("ver", "1.0")
				.queryParam("sidoName", sido) // 자동 인코딩
				.build()
				.encode(StandardCharsets.UTF_8)
				.toUri();
		
		System.out.println("[API] URL = " + url);

		RestTemplate restTemplate = new RestTemplate();
		String jsonResponse = restTemplate.getForObject(url, String.class);

		if (jsonResponse == null || jsonResponse.isBlank()) {
			System.err.println("[API] 빈 응답 (sido=" + sido + ")");
			return;
		}
		// 응답 코드/메시지 찍기
		try {
			JsonNode root0 = objectMapper.readTree(jsonResponse);
			String resultCode = root0.path("response").path("header").path("resultCode").asText();
			String resultMsg = root0.path("response").path("header").path("resultMsg").asText();
			if (!"00".equals(resultCode)) {
				System.err.println("[API] 오류코드=" + resultCode + ", 메시지=" + resultMsg + ", sido=" + sido);
			}
		} catch (Exception ignore) {
			// 무시
		}

		JsonNode root = objectMapper.readTree(jsonResponse);
		JsonNode items = root.path("response").path("body").path("items");
		if (!items.isArray())
			return;

		List<AirDto> batch = new ArrayList<>(items.size());
		int rawCnt = 0;

		for (JsonNode item : items) {
			rawCnt++;
			// dataTime 파싱 및 시단위 정규화
			String dataTimeRaw = item.path("dataTime").asText(null); // "yyyy-MM-dd HH:mm"
			if (dataTimeRaw == null || dataTimeRaw.isBlank())
				continue;

			LocalDateTime dataTime;
			try {
				dataTime = LocalDateTime.parse(dataTimeRaw, API_TIME).truncatedTo(ChronoUnit.HOURS); // 분/초 0으로
			} catch (Exception e) {
				// 포맷 이상치 스킵
				continue;
			}

			AirDto dto = new AirDto();
			dto.setSidoName(sido);
			dto.setStationName(item.path("stationName").asText(null));
			dto.setDataTime(dataTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00")));
			dto.setPm10Value(item.path("pm10Value").asText(null));
			dto.setPm25Value(item.path("pm25Value").asText(null));
			dto.setKhaiValue(item.path("khaiValue").asText(null));

			if (dto.getStationName() == null)
				continue;
			batch.add(dto);
		}

		System.out.println("[BATCH] sido=" + sido + ", API items=" + rawCnt + ", insert batch size=" + batch.size());

		if (!batch.isEmpty()) {
			airDao.upsertAirQualityBatch(batch);
			System.out.println("[DB] upsert 호출 완료 (sido=" + sido + ", size=" + batch.size() + ")");
		} else {
			System.err.println("[DB] 배치비어있음 (sido=" + sido + ")");
		}

		if (!batch.isEmpty()) {
			// UPSERT로 중복 방지
			airDao.upsertAirQualityBatch(batch);
		}
	}

	private Integer toInteger(String s) {
		if (s == null)
			return null;
		s = s.trim();
		if (s.isEmpty() || "-".equals(s))
			return null;
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private BigDecimal toBigDecimal(String s) {
		if (s == null)
			return null;
		s = s.trim();
		if (s.isEmpty() || "-".equals(s))
			return null;
		try {
			return new BigDecimal(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

//	@Transactional(readOnly = true)
//	public List<Airpm10AvgDto> selectPm10Trend(String sido) {
//		return airDao.selectPm10(sido);
//	}

}
