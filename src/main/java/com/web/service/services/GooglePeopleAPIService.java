package com.web.service.services;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.web.service.repositories.GooglePeopleAPIRepository;

@Service
public class GooglePeopleAPIService {

  @Autowired
  private GooglePeopleAPIRepository peopleAPIRepo;

  public List<Name> getPersonNames(String accessToken) {
    Person person = peopleAPIRepo.getSignedInUserInfo(accessToken);

    if (Objects.nonNull(person)) {
      return person.getNames();
    }

    return null;
  }

}
