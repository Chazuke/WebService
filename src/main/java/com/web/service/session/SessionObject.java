package com.web.service.session;

import java.io.Serializable;

public class SessionObject implements Serializable {
	private static final long serialVersionUID = -4967042187413609L;

	String name;
	int numFollowers;

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
}
