package com.web.service.google;

import com.web.service.google.objects.GooglePerson;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface GooglePeopleAPIFeignClient {

	@RequestLine("GET /v1/people/me")
	@Headers("Authorization: {accessToken}")
	GooglePerson getPersonalInfo(@Param(value = "accessToken") String accessToken);

}
