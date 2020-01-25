package com.web.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceGoogleConfig {
	
	@Value(value ="${google.authUrl}")
	private String authUrl;
	
	@Value(value = "${google.clientId}")
	private String clientId;
	
	@Value(value = "${google.redirectUrl}")
	private String redirectUrl;
	
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

	public String getAuthUrl() {
		return authUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
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

}
