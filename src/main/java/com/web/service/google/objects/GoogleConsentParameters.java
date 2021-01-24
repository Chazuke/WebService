package com.web.service.google.objects;

public class GoogleConsentParameters {

	private String client_id;
	private String redirect_uri;
	private String response_type;
	private String scope;
	private String access_type;
	private String state;
	private String include_granted_scopes;
	private String prompt;

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getRedirect_uri() {
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri) {
		this.redirect_uri = redirect_uri;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAccess_type() {
		return access_type;
	}

	public void setAccess_type(String access_type) {
		this.access_type = access_type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getInclude_granted_scopes() {
		return include_granted_scopes;
	}

	public void setInclude_granted_scopes(String include_granted_scopes) {
		this.include_granted_scopes = include_granted_scopes;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getParameterString() {
		StringBuilder paramString = new StringBuilder("?");
		paramString.append("&client_id=");
		paramString.append(getClient_id());
		paramString.append("&redirect_uri=");
		paramString.append(getRedirect_uri());
		paramString.append("&response_type=");
		paramString.append(getResponse_type());
		paramString.append("&scope=");
		paramString.append(getScope());
		paramString.append("&access_type=");
		paramString.append(getAccess_type());
		paramString.append("&state=");
		paramString.append(getState());
		paramString.append("&include_granted_scopes=");
		paramString.append(getInclude_granted_scopes());
		paramString.append("&prompt=");
		paramString.append(getPrompt());
		return paramString.toString();
	}

}
