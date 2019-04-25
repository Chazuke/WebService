package com.web.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.service.session.SessionClient;
import com.web.service.session.SessionObject;

@RestController
@RequestMapping(value = "/api")
public class WebServiceController {
	private static Logger LOG = LoggerFactory.getLogger(WebServiceController.class);

	@Autowired
	SessionClient sessionClient;

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

}
