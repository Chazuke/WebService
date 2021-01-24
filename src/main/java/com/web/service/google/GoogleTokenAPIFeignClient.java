package com.web.service.google;

import com.web.service.google.objects.GoogleAccessTokenResponse;

import feign.Headers;
import feign.RequestLine;

public interface GoogleTokenAPIFeignClient {
	@RequestLine("POST /token")
	@Headers("Content-Type: application/x-www-form-urlencoded")
	GoogleAccessTokenResponse getGoogleAccessToken(String gatRequest);
}