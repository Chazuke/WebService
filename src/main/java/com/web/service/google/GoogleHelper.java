package com.web.service.google;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.service.config.WebServiceGoogleConfig;
import com.web.service.session.SessionClient;
import com.web.service.session.SessionObject;

@Component
public class GoogleHelper {
	Logger LOG = LoggerFactory.getLogger(GoogleHelper.class);

	@Autowired
	SessionClient sessionClient;

	@Autowired
	WebServiceGoogleConfig googleConfig;

	@Autowired
	RestTemplate restTemplate;

	private static final String CLIENT_SECRET_PATH = "C:\\git\\Chazuke\\Secrets\\WebService\\";
	private static final String CLIENT_SECRET_FILENAME = "client_secret_427896572355-gqpckukc9gtuqk82oh8hm6kks61krim1.apps.googleusercontent.com.json";

	public boolean setSessionCallbackUUID(String sessionId, String uuid) {
		boolean response = false;
		try {
			SessionObject sessionObject = sessionClient.ensureSessionObjectExists(sessionId);
			sessionObject.setGoogleUUID(uuid);
			response = sessionClient.set(sessionId, sessionObject).get();
		} catch (Exception e) {
			LOG.error("Exception occured while updating google callback UUID into session", e);
		}
		return response;
	}

	public boolean verifySessionCallbackUUID(String sessionId, String uuid) {
		boolean response = false;
		try {
			SessionObject sessionObject = sessionClient.ensureSessionObjectExists(sessionId);
			if (uuid.equals(sessionObject.getGoogleUUID())) {
				response = true;
			}
		} catch (Exception e) {
			LOG.error("Exception occured while verifying google callback UUID from session", e);
		}
		return response;
	}

	public String getAccessTokens(String authCode) {
		GoogleAccessTokenRequest tokenRequest = new GoogleAccessTokenRequest();
		tokenRequest.setCode(authCode);
		tokenRequest.setClient_id(googleConfig.getClientId());
		tokenRequest.setClient_secret(getClientSecret());
		tokenRequest.setRedirect_uri(googleConfig.getRedirectUrlToken());
		tokenRequest.setGrant_type(googleConfig.getGrantType());

		LOG.info("Exit getGoogleConsentResult");

		return "";
	}

	private String getClientSecret() {
		String client_secret = null;
		try (Scanner fsc = new Scanner(new File(CLIENT_SECRET_PATH + CLIENT_SECRET_FILENAME))) {
			LOG.info("Found Client Secret File");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode secretObj = mapper.readTree(fsc.nextLine());
			LOG.debug("Client Secret Object: {}" + secretObj.toString());
			client_secret = secretObj.findValue("client_secret").asText();
		} catch (Exception e) {
			LOG.error("Exception occured while attempting to read client secret from file", e);
		}
		return client_secret;
	}

	public String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}

}
