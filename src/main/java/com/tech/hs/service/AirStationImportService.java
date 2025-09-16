package com.tech.hs.service;

import java.util.Iterator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.hs.dao.AirStationDao;
import com.tech.hs.dto.AirStationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirStationImportService {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final AirStationDao stationDao;

	@Transactional
	public int importFromJson(String json) throws Exception {
		if (json == null || json.isBlank()) {
			throw new IllegalArgumentException("stations.json 내용이 비어있습니다.");
		}
		// XML/HTML 을 JSON으로 잘못 읽는 상황 방지
		char first = firstNonWhitespace(json);
		if (first == '<') {
			throw new IllegalStateException("JSON이 아니라 XML/HTML(<...)을 읽었습니다. stations.json 파일/경로를 확인하세요.");
		}

		JsonNode root = objectMapper.readTree(json);
		if (!root.isArray()) {
			throw new IllegalArgumentException("stations.json 최상위는 배열이어야 합니다.");
		}

		int count = 0;
		Iterator<JsonNode> it = root.elements();
		while (it.hasNext()) {
			JsonNode n = it.next();

			String name = reqText(n, "stationName");
			String addr = optText(n, "addr");
			double lat = reqDouble(n, "dmX");
			double lon = reqDouble(n, "dmY");

			AirStationDto dto = new AirStationDto();
			dto.setStationName(name);
			dto.setAddr(addr);
			dto.setLat(lat);
			dto.setLng(lon);

			stationDao.upsert(dto);
			count++;
		}
		return count;
	}

	private static char firstNonWhitespace(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isWhitespace(s.charAt(i)))
				return s.charAt(i);
		}
		return '\0';
	}

	private static String reqText(JsonNode n, String field) {
		JsonNode v = n.get(field);
		if (v == null || v.isNull() || v.asText().isBlank()) {
			throw new IllegalArgumentException("필수 필드 누락: " + field);
		}
		return v.asText();
	}

	private static String optText(JsonNode n, String field) {
		JsonNode v = n.get(field);
		return (v == null || v.isNull()) ? null : v.asText();
	}

	private static double reqDouble(JsonNode n, String field) {
		JsonNode v = n.get(field);
		if (v == null || v.isNull()) {
			throw new IllegalArgumentException("필수 필드 누락: " + field);
		}
		if (v.isNumber())
			return v.asDouble();
		// 문자열일 경우 파싱 시도
		String s = v.asText();
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("숫자 필드 파싱 실패: " + field + "=" + s);
		}
	}
}
