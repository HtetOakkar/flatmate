package com.lotus.flatmate.configs;

import java.util.TimeZone;
import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean(name = "TaskExecutor")
	Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(25);
		executor.setMaxPoolSize(25);
		executor.setKeepAliveSeconds(3200);
		executor.setThreadNamePrefix("PostThread-");
		executor.setQueueCapacity(50);
		executor.initialize();
		return executor;
	}

	@Bean
	ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(5);
		taskScheduler.setThreadNamePrefix("websocket-heartbeat-scheduler-");
		taskScheduler.initialize();
		return taskScheduler;
	}

}
