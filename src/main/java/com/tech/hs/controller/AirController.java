package com.tech.hs.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tech.hs.dao.AirDao;
import com.tech.hs.service.AirService;

@Controller
@RequestMapping("/hs/api")
public class AirController {

	private static final Logger log = LoggerFactory.getLogger(AirController.class);

	private final AirService airService;
	private final AirDao airDao;

	public AirController(AirService airService, AirDao airDao) {
		this.airService = airService;
		this.airDao = airDao;
	}

	@GetMapping("/fetch-now")
	public String fetchNow() throws Exception {
		for (String sido : List.of("서울", "경기", "부산", "대전")) {
			airService.fetchAndSaveAirQuality(sido);
		}
		return "ok";
	}

	// 매시 30분마다 실행
	@Scheduled(cron = "0 30 * * * *", zone = "Asia/Seoul")
	public void scheduledTaskHourly() {
		String[] sidos = { "서울", "경기", "부산", "대전" };

		for (String sido : sidos) {
			try {
				airService.fetchAndSaveAirQuality(sido);
				log.info("{} 데이터 저장 성공 at {}", sido, LocalDateTime.now());
			} catch (Exception ex) {
				log.error("저장 실패: {}", sido, ex);
			}

			// API 과부하 방지
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // 인터럽트 상태 복원
				log.warn("스케줄러 sleep 중단됨", e);
			}
		}
	}
}
