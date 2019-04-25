package com.web.service;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.web.service.session.SessionClient;

@Configuration
@EnableWebMvc
public class WebServiceConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	ServletWebServerFactory servletWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

	@Bean(destroyMethod = "destroy")
	public SessionClient initializeSessionClient() throws Exception {
		return new SessionClient();
	}

}
