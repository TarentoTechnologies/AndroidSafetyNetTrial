package com.tarento.safetynet.bean;

public class NonceResponse extends Response {

	private String nonce;

	public NonceResponse(String nonce) {
		super();
		this.nonce = nonce;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
}
