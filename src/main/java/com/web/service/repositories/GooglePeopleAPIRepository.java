package com.web.service.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.google.api.services.people.v1.model.Person;
import com.web.service.feign.GooglePeopleAPIFeignClient;

@Repository
public class GooglePeopleAPIRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(GooglePeopleAPIRepository.class);

  @Autowired
  private GooglePeopleAPIFeignClient peopleAPIFeignClient;

  public Person getSignedInUserInfo(String accessToken) {

    Person person = null;

    try {
      person = peopleAPIFeignClient.getPersonInfoUsingGET(accessToken);
    } catch (Exception e) {
      LOGGER.error("Exception occurred while attempting to fetch Person details from Google", e);
    }

    return person;

  }

}
