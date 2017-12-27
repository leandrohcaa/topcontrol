package com.topcontrol;

import com.topcontrol.domain.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = BaseEntity.class)
@SpringBootApplication
public class TopControlApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TopControlApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(TopControlApplication.class, args);
	}
}
