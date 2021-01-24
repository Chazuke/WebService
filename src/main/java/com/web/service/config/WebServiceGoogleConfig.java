package com.web.service.config;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.google.gson.Gson;
import com.web.service.google.GooglePeopleAPIFeignClient;
import com.web.service.google.GoogleTokenAPIFeignClient;
import com.web.service.google.objects.GoogleSecretWrapper;

import feign.Feign;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

@Configuration
public class WebServiceGoogleConfig {

	private static Logger LOG = LoggerFactory.getLogger(WebServiceGoogleConfig.class);

	@Value(value = "${google.authUrl}")
	private String authUrl;

	@Value(value = "${google.clientId}")
	private String clientId;

	@Value(value = "${google.redirectUrlConsent}")
	private String redirectUrlConsent;

	@Value(value = "${google.responseType}")
	private String responseType;

	@Value(value = "${google.scope.userEmail}")
	private String scopeUserEmail;

	@Value(value = "${google.scope.userProfile}")
	private String scopeUserProfile;

	@Value(value = "${google.accessType}")
	private String accessType;

	@Value(value = "${google.includeGrantedScopes}")
	private String includeGrantedScopes;

	@Value(value = "${google.prompt.selectAccount}")
	private String promptSelectAccount;

	@Value(value = "${google.prompt.consent}")
	private String promptConsent;

	@Value(value = "${google.redirectUrlToken}")
	private String redirectUrlToken;

	@Value(value = "${google.tokenUrl}")
	private String tokenUrl;

	@Value(value = "${google.grantType}")
	private String grantType;

	@Value(value = "${google.clientSecretPath}")
	private String clientSecretPath;

	@Value(value = "${google.peopleUrl}")
	private String personUrl;

	private GoogleSecretWrapper clientSecret;

	public String getAuthUrl() {
		return authUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public String getRedirectUrlConsent() {
		return redirectUrlConsent;
	}

	public String getResponseType() {
		return responseType;
	}

	public String getScopeUserEmail() {
		return scopeUserEmail;
	}

	public String getScopeUserProfile() {
		return scopeUserProfile;
	}

	public String getAccessType() {
		return accessType;
	}

	public String getIncludeGrantedScopes() {
		return includeGrantedScopes;
	}

	public String getPromptSelectAccount() {
		return promptSelectAccount;
	}

	public String getPromptConsent() {
		return promptConsent;
	}

	public String getRedirectUrlToken() {
		return redirectUrlToken;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public String getGrantType() {
		return grantType;
	}

	public GoogleSecretWrapper getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(GoogleSecretWrapper googleClientSecret) {
		this.clientSecret = googleClientSecret;
	}

	public String getClientSecretPath() {
		return clientSecretPath;
	}

	public void setClientSecretPath(String clientSecretPath) {
		this.clientSecretPath = clientSecretPath;
	}

	public String getPersonUrl() {
		return personUrl;
	}

	public void setPersonUrl(String personUrl) {
		this.personUrl = personUrl;
	}

	@PostConstruct
	private void readSecret() {
		LOG.info("Attempting to read client secret from path {}", getClientSecretPath());
		try (FileReader clientSecretFR = new FileReader(getClientSecretPath());
				BufferedReader secretFileBR = new BufferedReader(clientSecretFR);) {
			String secretJsonLine = secretFileBR.readLine();
			setClientSecret(new Gson().fromJson(secretJsonLine, GoogleSecretWrapper.class));
		} catch (Exception e) {
			LOG.error("Exception occured while attempting to read client secret from file!", e);
		}
	}

	@Bean
	@Lazy
	@Qualifier("GoogleTokenAPIFeignClient")
	GoogleTokenAPIFeignClient getGoogleTokenAPIFeignClient() {
		return Feign.builder().client(new OkHttpClient()).encoder(new FormEncoder()).decoder(new GsonDecoder())
				.logger(new Slf4jLogger(GoogleTokenAPIFeignClient.class))
				.target(GoogleTokenAPIFeignClient.class, getTokenUrl());
	}

	@Bean
	@Lazy
	@Qualifier("GooglePeopleAPIFeignClient")
	GooglePeopleAPIFeignClient getGooglePeopleAPIFeignClient() {
		return Feign.builder().client(new OkHttpClient()).encoder(new GsonEncoder()).decoder(new GsonDecoder())
				.logger(new Slf4jLogger(GooglePeopleAPIFeignClient.class))
				.target(GooglePeopleAPIFeignClient.class, getPersonUrl());
	}

}
