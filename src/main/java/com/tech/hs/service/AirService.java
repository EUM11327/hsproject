package com.tech.hs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AirService {
    private final RestTemplate restTemplate;

    public AirService(org.springframework.boot.web.client.RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(java.time.Duration.ofMillis(2500))
                .setReadTimeout(java.time.Duration.ofMillis(2500))
                .build();
    }

    public String getAirData(String sidoName) {
        String serviceKey = "5a9f2ede48b1ba5dede90f126eae8a9469568790595cd42b880b61c80480eddd";

        java.net.URI uri = org.springframework.web.util.UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty")
                .queryParam("serviceKey", serviceKey)
                .queryParam("returnType", "json")
                .queryParam("sidoName", sidoName)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("ver", "1.0")
                .encode()
                .build(false)
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        System.out.println("=================== api 데이터 ===================");
        System.out.println(response);
        
        return response;
    }
}


