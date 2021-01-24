package com.web.service.google.objects;

import java.io.Serializable;

public class GoogleAccessTokenRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1153716899407289085L;

	private String client_id;
	private String client_secret;
	private String code;
	private String grant_type;
	private String redirect_uri;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("code=");
		builder.append(getCode());
		builder.append("&client_id=");
		builder.append(getClient_id());
		builder.append("&client_secret=");
		builder.append(getClient_secret());
		builder.append("&redirect_uri=");
		builder.append(getRedirect_uri());
		builder.append("&grant_type=");
		builder.append(getGrant_type());
		return builder.toString();
	}

}
