package com.tech.hs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.tech.hs.dao")
public class HsprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(HsprojectApplication.class, args);
	}

}
