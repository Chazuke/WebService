package com.web.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import com.google.api.services.people.v1.model.Person;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@FeignClient(value = "GooglePeopleAPIFeignClient")
public interface GooglePeopleAPIFeignClient {

  @RequestLine("GET /people/me?personFields=names")
  @Headers({"Authorization: BEARER {accessToken}", "Accept: application/json",
      "Content-Type: application/json"})
  Person getPersonInfoUsingGET(@Param("accessToken") String accessToken);
}
