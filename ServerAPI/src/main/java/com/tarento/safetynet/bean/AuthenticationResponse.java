package com.tarento.safetynet.bean;

public class AuthenticationResponse extends Response {
	private boolean valid;

	public AuthenticationResponse(boolean valid) {
		super();
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
}
