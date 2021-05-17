package com.web.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.web.service.feign.GooglePeopleAPIFeignClient;
import feign.Feign;

@Configuration
public class ApplicationFeignConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationFeignConfig.class);

  @Bean
  public GooglePeopleAPIFeignClient googlePeopleAPIFeignClient(
      @Value("${services.google.people.uri}") String url) {
    LOGGER.info("Creating GooglePeopleAPIFeignClient with target URL: {}", url);
    return Feign.builder().target(GooglePeopleAPIFeignClient.class, url);
  }

}
