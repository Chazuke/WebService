package com.web.service.session;

import java.io.Serializable;

public class SessionObject implements Serializable {
	private static final long serialVersionUID = -4967042187413609L;
	
	String sessionId;
	String name;
	String googleUUID;
	int numFollowers;
	
	public SessionObject () {}
	
	public SessionObject(String sessionId) {
		setSessionId(sessionId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(int numFollowers) {
		this.numFollowers = numFollowers;
	}

	public String getGoogleUUID() {
		return googleUUID;
	}

	public void setGoogleUUID(String googleUUID) {
		this.googleUUID = googleUUID;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
