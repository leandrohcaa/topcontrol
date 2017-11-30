package com.topcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

import com.topcontrol.domain.BaseEntity;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = BaseEntity.class)
@SpringBootApplication
public class TopControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopControlApplication.class, args);
	}
}
