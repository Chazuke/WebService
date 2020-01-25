package com.web.service.controller;

import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.web.service.config.WebServiceGoogleConfig;
import com.web.service.session.SessionClient;
import com.web.service.session.SessionObject;
import com.web.service.session.User;

@RestController
@RequestMapping(value = "/api")
public class WebServiceController {
	private static Logger LOG = LoggerFactory.getLogger(WebServiceController.class);

	@Autowired
	SessionClient sessionClient;
	
	@Autowired
	WebServiceGoogleConfig googleConfig;

	@GetMapping("/hello")
	public String helloWorld() throws Exception {
		LOG.debug("Setting info into cache");
		sessionClient.set("Hello", "Hello, World!");
		LOG.debug("Done.");
		LOG.debug("Retrieving info from cache");
		String hello = (String) sessionClient.get("Hello");
		LOG.debug("Done.");
		return hello;
	}

	@GetMapping("/login")
	public String login() throws Exception {
		SessionObject sessObj = new SessionObject();
		sessObj.setName("Scott McClellan");
		sessObj.setNumFollowers(123456789);
		LOG.debug("Setting object into memcached");
		sessionClient.set("SSID", sessObj);
		return "Success";
	}

	@GetMapping("/getName")
	public String getName() throws Exception {
		LOG.debug("Retrieving object from memcached");
		SessionObject retObj = (SessionObject) sessionClient.get("SSID");
		return retObj.getName();
	}
	
	@GetMapping("/signin")
	public RedirectView getGoogleConsentURL() throws UnsupportedEncodingException {
		StringBuilder redirectURL = new StringBuilder(googleConfig.getAuthUrl());
		redirectURL.append("?client_id=" + googleConfig.getClientId());
		redirectURL.append(
				"&redirect_uri=" + URLEncoder.encode(googleConfig.getRedirectUrl(), StandardCharsets.UTF_8.toString()));
		redirectURL.append("&response_type=" + googleConfig.getResponseType());
		
		redirectURL.append("&scope=" + URLEncoder.encode(
				googleConfig.getScopeUserEmail() + " " + googleConfig.getScopeUserProfile(),
				StandardCharsets.UTF_8.toString()));
		
		redirectURL.append("&access_type=" + googleConfig.getAccessType());
		//TODO: Set this UUID into Redis session and check it against google consent callback
		redirectURL.append("&state=" + UUID.randomUUID());
		redirectURL.append("&include_granted_scopes=" + googleConfig.getIncludeGrantedScopes());
		
		redirectURL.append("&prompt="
				+ URLEncoder.encode(googleConfig.getPromptSelectAccount() + " " + googleConfig.getPromptConsent(),
						StandardCharsets.UTF_8.toString()));
		
		LOG.info("Constructed Google Consent Redirect URL:\n{}", redirectURL.toString());
		
		return new RedirectView(redirectURL.toString());
	}
	
	@GetMapping("/signinresult")
	public String getGoogleConsentResult(HttpServletRequest request) {
		return request.getParameter("code") != null ? request.getParameter("code") : request.getParameter("error");
	}

	@PostMapping
	public String googleSignIn(HttpServletRequest request, HttpServletResponse response, String authCode)
			throws Exception {
		if (request.getHeader("X-Requested-With") == null) {
			// Without the `X-Requested-With` header, this request could be forged. Aborts.
		}

		// Set path to the Web application client_secret_*.json file you downloaded from
		// the
		// Google API Console: https://console.developers.google.com/apis/credentials
		// You can also find your Web application client ID and client secret from the
		// console and specify them directly when you create the
		// GoogleAuthorizationCodeTokenRequest
		// object.
		String CLIENT_SECRET_FILE = "/path/to/client_secret.json";

		// Exchange auth code for access token
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
				new FileReader(CLIENT_SECRET_FILE));
		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance(), "https://www.googleapis.com/oauth2/v4/token",
				clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), authCode, "")
						.execute();

		// Get profile info from ID token
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();

		User newUser = new User();
		newUser.setUserId(payload.getSubject());
		newUser.setEmail(payload.getEmail());
		newUser.setEmailVerified(Boolean.valueOf(payload.getEmailVerified()));
		newUser.setName((String) payload.get("name"));
		newUser.setPictureUrl((String) payload.get("picture"));
		newUser.setLocale((String) payload.get("locale"));
		newUser.setFamilyName((String) payload.get("family_name"));
		newUser.setGivenName((String) payload.get("given_name"));

		UUID uuid = UUID.randomUUID();
		sessionClient.set(uuid.toString(), newUser);
		return uuid.toString();
	}

}
