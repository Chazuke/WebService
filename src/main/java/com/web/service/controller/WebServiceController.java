package com.web.service.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.web.service.config.WebServiceGoogleConfig;
import com.web.service.google.GoogleAccessTokenRequest;
import com.web.service.google.GoogleAccessTokenResponse;
import com.web.service.google.GoogleFeignClient;
import com.web.service.google.GoogleHelper;
import com.web.service.session.SessionClient;

@RestController
@RequestMapping(value = "/api")
public class WebServiceController {
	private static Logger LOG = LoggerFactory.getLogger(WebServiceController.class);

	@Autowired
	SessionClient sessionClient;

	@Autowired
	WebServiceGoogleConfig googleConfig;

	@Autowired
	GoogleHelper googleHelper;

	@Autowired
	GoogleFeignClient googleFeignClient;

	@GetMapping("/signin")
	public RedirectView getGoogleConsentURL(HttpServletRequest request) throws Exception {
		LOG.info("Enter getGoogleConsentURL");
		StringBuilder redirectURL = new StringBuilder(googleConfig.getAuthUrl());
		redirectURL.append("?client_id=" + googleConfig.getClientId());
		redirectURL.append("&redirect_uri="
				+ URLEncoder.encode(googleConfig.getRedirectUrlConsent(), StandardCharsets.UTF_8.toString()));
		redirectURL.append("&response_type=" + googleConfig.getResponseType());

		redirectURL.append("&scope="
				+ URLEncoder.encode(googleConfig.getScopeUserEmail() + " " + googleConfig.getScopeUserProfile(),
						StandardCharsets.UTF_8.toString()));

		redirectURL.append("&access_type=" + googleConfig.getAccessType());

		String googleUUID = UUID.randomUUID().toString();
		boolean iscallbackUuidSuccess = googleHelper.setSessionCallbackUUID(request.getSession().getId(), googleUUID);
		if (!iscallbackUuidSuccess) {
			throw new Exception("Internal Server Error");
		}
		redirectURL.append("&state=" + googleUUID);

		redirectURL.append("&include_granted_scopes=" + googleConfig.getIncludeGrantedScopes());

//		redirectURL.append("&prompt="
//				+ URLEncoder.encode(googleConfig.getPromptSelectAccount() + " " + googleConfig.getPromptConsent(),
//						StandardCharsets.UTF_8.toString()));

		LOG.debug("Constructed Google Consent Redirect URL:\n{}", redirectURL.toString());

		return new RedirectView(redirectURL.toString());
	}

	@GetMapping("/fetchAccessTokens")
	public GoogleAccessTokenResponse getGoogleAccessToken(HttpServletRequest request) throws Exception {
		LOG.info("Enter getGoogleConsentResult");

		if (request.getParameter("code") != null) {

			GoogleAccessTokenRequest tokenRequest = new GoogleAccessTokenRequest();
			tokenRequest.setClient_id(googleConfig.getClientId());
			tokenRequest.setClient_secret(googleConfig.getClientSecret().getWeb().getClient_secret());
			tokenRequest.setCode(request.getParameter("code"));
			tokenRequest.setGrant_type(googleConfig.getGrantType());
			tokenRequest.setRedirect_uri(googleConfig.getRedirectUrlConsent());

			LOG.info("Fully Google Access Token Request Body String:\n {}", tokenRequest.toString());

			GoogleAccessTokenResponse response = googleFeignClient.getGoogleAccessToken(tokenRequest.toString());
			return response;
		} else {
			return null;
		}

	}

	@PostMapping("/home")
	public ModelAndView postHomePage(HttpServletRequest request) throws IOException {
		return new ModelAndView(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
	}

	@GetMapping("/home")
	public ModelAndView getHomePage(HttpServletRequest request) throws IOException {
		return new ModelAndView(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
	}
}
