package com.portmate.global.kafka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
	// Spring Boot 자동 설정 사용
	// application.yml의 spring.kafka.* 설정이 자동으로 적용됩니다
}
