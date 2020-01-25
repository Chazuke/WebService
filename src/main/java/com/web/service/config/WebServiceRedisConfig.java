package com.web.service.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.web.service.session.SessionClient;

@Configuration
public class WebServiceRedisConfig {
	private static Logger LOG = LoggerFactory.getLogger(WebServiceRedisConfig.class);
	
	@Value(value = "${session.address}")
	private String sessionManagerAddress;
	
	@Value(value = "${session.port}")
	private int sessionManagerPort;
	
	@Bean(destroyMethod = "destroy")
	public SessionClient initializeSessionClient() {
		LOG.info("Creating Session Client...");
		SessionClient sessionClient = null;
		try {
			sessionClient = new SessionClient(sessionManagerAddress, sessionManagerPort);
		} catch (IOException e) {
			LOG.error("Exception occured while creating session client!", e);
		}
		LOG.info("Session Client Creation Successful! Bound Session Service at {}:{}", sessionManagerAddress,
				sessionManagerPort);
		return sessionClient;
	}

}
