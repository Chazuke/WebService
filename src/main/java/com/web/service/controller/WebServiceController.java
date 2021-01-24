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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.web.service.config.WebServiceGoogleConfig;
import com.web.service.google.GoogleHelper;
import com.web.service.google.GooglePeopleAPIFeignClient;
import com.web.service.google.GoogleTokenAPIFeignClient;
import com.web.service.google.objects.GoogleAccessTokenRequest;
import com.web.service.google.objects.GoogleAccessTokenResponse;
import com.web.service.google.objects.GoogleConsentParameters;
import com.web.service.google.objects.GoogleName;
import com.web.service.google.objects.GooglePerson;
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
	@Qualifier("GoogleTokenAPIFeignClient")
	GoogleTokenAPIFeignClient googleTokenAPIFeignClient;

	@Autowired
	@Qualifier("GooglePeopleAPIFeignClient")
	GooglePeopleAPIFeignClient googlePeopleAPIFeignClient;

	@GetMapping("/signin")
	public RedirectView getGoogleConsentURL(HttpServletRequest request) throws Exception {
		LOG.info("Enter getGoogleConsentURL");

		GoogleConsentParameters params = new GoogleConsentParameters();
		params.setClient_id(googleConfig.getClientId());
		params.setRedirect_uri(
				URLEncoder.encode(googleConfig.getRedirectUrlConsent(), StandardCharsets.UTF_8.toString()));
		params.setResponse_type(googleConfig.getResponseType());
		params.setScope(URLEncoder.encode(googleConfig.getScopeUserEmail() + " " + googleConfig.getScopeUserProfile(),
				StandardCharsets.UTF_8.toString()));
		params.setAccess_type(googleConfig.getAccessType());

		String googleUUID = UUID.randomUUID().toString();
		boolean iscallbackUuidSuccess = googleHelper.setSessionCallbackUUID(request.getSession().getId(), googleUUID);
		if (!iscallbackUuidSuccess) {
			throw new Exception("Internal Server Error");
		}

		params.setState(googleUUID);
		params.setInclude_granted_scopes(googleConfig.getIncludeGrantedScopes());
		params.setPrompt(
				URLEncoder.encode(googleConfig.getPromptSelectAccount() + " " + googleConfig.getPromptConsent(),
						StandardCharsets.UTF_8.toString()));

		LOG.debug("Constructed Google Consent Redirect URL:\n{}",
				googleConfig.getAuthUrl() + params.getParameterString());

		return new RedirectView(googleConfig.getAuthUrl() + params.getParameterString());
	}

	@GetMapping("/fetchAccessTokens")
	public GoogleAccessTokenResponse getGoogleAccessToken(HttpServletRequest request) throws Exception {
		LOG.info("Enter getGoogleConsentResult");

		if (request.getParameter("code") != null && googleHelper.verifySessionCallbackUUID(request.getSession().getId(),
				request.getParameter("state"))) {

			GoogleAccessTokenRequest tokenRequest = new GoogleAccessTokenRequest();
			tokenRequest.setClient_id(googleConfig.getClientId());
			tokenRequest.setClient_secret(googleConfig.getClientSecret().getWeb().getClient_secret());
			tokenRequest.setCode(request.getParameter("code"));
			tokenRequest.setGrant_type(googleConfig.getGrantType());
			tokenRequest.setRedirect_uri(googleConfig.getRedirectUrlConsent());

			LOG.info("Google Access Token Request:\n {}", tokenRequest.toString());

			GoogleAccessTokenResponse response = googleTokenAPIFeignClient
					.getGoogleAccessToken(tokenRequest.toString());

			if (null != response && null != response.getAccess_token()) {
				GooglePerson userData = googlePeopleAPIFeignClient.getPersonalInfo(response.getAccess_token());
				GoogleName[] names = userData.getNames();
				LOG.info("Found Names: {}", names.toString());
			}

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
