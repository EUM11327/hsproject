package com.tech.hs.boot;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tech.hs.service.AirStationImportService;

@Configuration
public class StationBootstrapRunner {

    private static final Logger log = LoggerFactory.getLogger(StationBootstrapRunner.class);
    private static final String RESOURCE_PATH = "/stations.json";

    @Bean
    ApplicationRunner loadStationsFromJson(AirStationImportService importer) {
        return args -> {
            try (InputStream in = getClass().getResourceAsStream(RESOURCE_PATH)) {
                if (in == null) {
                    log.warn("[BOOT][STATION] {} 파일을 찾지 못했습니다. (건너뜀)", RESOURCE_PATH);
                    return;
                }
                String body = new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();

                char first = body.isEmpty() ? '\0' : body.charAt(0);
                if (first != '{' && first != '[') {
                    log.error("[BOOT][STATION] JSON이 아닌 형식 감지(첫 문자='{}'). 파일 내용을 확인하세요. (건너뜀)", first);
                    return;
                }

                int count = importer.importFromJson(body); // 내부 파싱/저장
                log.info("[BOOT][STATION] stations.json 적재 완료: {}건", count);
            } catch (Exception e) {
                log.error("[BOOT][STATION] stations.json 적재 중 오류. 부팅은 계속합니다.", e);
            }
        };
    }
}
