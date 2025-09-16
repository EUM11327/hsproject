package com.tech.hs.boot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tech.hs.service.AirStationImportService;

@Configuration
public class StationFileBootstrap {

	@Bean
	ApplicationRunner loadStationsFromFile(AirStationImportService importer) {
		return args -> {
			try (var in = StationFileBootstrap.class.getResourceAsStream("/stations.json")) {
				if (in == null) {
					System.err.println("[BOOT] stations.json 없음 - 초기 적재 생략");
					return;
				}
				String json = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);

				if (!json.isEmpty() && json.charAt(0) == '\uFEFF') {
					json = json.substring(1);
				}

				int c = importer.importFromJson(json);
				System.out.println("[BOOT] stations.json 적재 완료: " + c + "건");
			} catch (Exception e) {
				System.err.println("[BOOT] stations.json 적재 실패: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}
}
