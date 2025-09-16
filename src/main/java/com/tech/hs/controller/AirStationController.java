package com.tech.hs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.hs.dao.AirStationDao;
import com.tech.hs.service.AirService;
import com.tech.hs.service.AirStationImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hs/air")
public class AirStationController {

    private final AirStationImportService importService;
    private final AirStationDao stationDao;
    private final AirService airService;

    @PostMapping(value = "/stations/import", consumes = "application/json", produces = "application/json")
    public java.util.Map<String, Object> importJson(@RequestBody String json) throws Exception {
        int upserts = importService.importFromJson(json);
        return java.util.Map.of("upserts", upserts);
    }

    // DB 좌표 실시간 농도
    @GetMapping(value = "/stations/{stationName}/realtime", produces = "application/json")
    public ResponseEntity<?> stationRealtime(@PathVariable String stationName) {
        var s = stationDao.findByName(stationName);
        if (s == null) {
            return ResponseEntity.status(404).body(java.util.Map.of(
                "error", "station not found",
                "stationName", stationName
            ));
        }

        // 공공데이터 실시간
        String raw = airService.getRealtimeByStation(stationName);
        if (raw == null || raw.isBlank()) {
            return ResponseEntity.status(502).body(java.util.Map.of(
                "error", "empty upstream response",
                "stationName", stationName
            ));
        }

        final var mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(raw);
        } catch (Exception e) {
            return ResponseEntity.status(502).body(java.util.Map.of(
                "error", "non-JSON upstream response",
                "message", e.getMessage(),
                "rawHead", raw.substring(0, Math.min(300, raw.length()))
            ));
        }

        var items = node.path("response").path("body").path("items");
        if (!items.isArray() || items.size() == 0) {
            return ResponseEntity.ok(java.util.Map.of(
                "station", s,
                "realtime", java.util.Map.of(
                    "dataTime", null,
                    "pm10", null,
                    "pm25", null
                ),
                "note", "no items from upstream"
            ));
        }

        var item = items.get(0);
        String dataTime = item.path("dataTime").asText(null);
        String pm10 = textOrNull(item, "pm10Value");
        String pm25 = textOrNull(item, "pm25Value");

        var out = new java.util.LinkedHashMap<String, Object>();
        out.put("station", s);
        var values = new java.util.LinkedHashMap<String, Object>();
        values.put("dataTime", dataTime);
        values.put("pm10", (pm10 != null && !pm10.equals("-")) ? pm10 : null);
        values.put("pm25", (pm25 != null && !pm25.equals("-")) ? pm25 : null);
        out.put("realtime", values);

        return ResponseEntity.ok(out);
    }

    private static String textOrNull(JsonNode n, String field) {
        var v = n.path(field).asText(null);
        return (v == null || v.isBlank()) ? null : v;
    }
}
