// src/main/java/com/tech/hs/controller/AirController.java
package com.tech.hs.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.hs.dao.AirDao;
import com.tech.hs.dto.Airpm10AvgDto;
import com.tech.hs.service.AirService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hs/service")
public class AirController {

	private final AirService airService;
	private final AirDao airDao;

	// 스케줄러가 주기적으로 실행하는 메소드
	@Scheduled(fixedRate = 3600000) // 1시간 마다 실행
	public void scheduledTask() {
		String[] sidos = { "서울", "경기", "부산", "대전" };
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try {

			for (String sido : sidos) {
				airService.fetchAndSaveAirQuality(sido);
				System.out.println(sido + " 데이터 저장 성공.  " + System.currentTimeMillis());
				String now = LocalDateTime.now().format(formatter);
				System.out.println(sido + " 데이터 저장 성공. " + now);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("저장중 에러발생");
		}

	}
	
	@GetMapping("/trendchart")
    public String trendChart(Model model) {
    	List<Airpm10AvgDto> pm10List = airDao.selectPm10("경기");
        System.out.println("==== pm10TrendList 확인 ====");
        for (Airpm10AvgDto dto : pm10List) {
            System.out.println(dto);
        }
        model.addAttribute("pm10TrendList", pm10List);
        return "pm10";
    }

}
