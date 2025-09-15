package com.tech.hs.job;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import com.tech.hs.service.AirService;

@Component
public class AirScheduler {

	private static final Logger log = LoggerFactory.getLogger(AirScheduler.class);
	private static final String[] SIDO_LIST = { "서울", "경기", "부산", "대전" };

	private final AirService airService;

	public AirScheduler(AirService airService) {
		this.airService = airService;
	}

	/** 앱 시작 후 1회 즉시 실행 */
	@EventListener(ApplicationReadyEvent.class)
	public void runOnceOnStartup() {
		runAllSidos("startup");
	}

	/** 매시 35분에 실행 */
	@Scheduled(cron = "${air.cron:0 30 * * * *}", zone = "Asia/Seoul")
	public void runHourly() {
		runAllSidos("hourly");
	}

	private void runAllSidos(String tag) {
		for (String sido : SIDO_LIST) {
			try {
				airService.fetchAndSaveAirQuality(sido);
				log.info("[{}] {} 데이터 저장 성공 at {}", tag, sido, LocalDateTime.now());
				Thread.sleep(200); // API 과부하 방지
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				log.warn("[{}] sleep 인터럽트", tag, ie);
			} catch (Exception ex) {
				log.error("[{}] 저장 실패: {}", tag, sido, ex);
			}
		}
	}
}
