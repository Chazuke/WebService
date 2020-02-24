package com.web.service.google;

import feign.Headers;
import feign.RequestLine;

public interface GoogleFeignClient {
	@RequestLine("POST")
	@Headers("Content-Type: application/x-www-form-urlencoded")
	GoogleAccessTokenResponse getGoogleAccessToken(String gatRequest);
}