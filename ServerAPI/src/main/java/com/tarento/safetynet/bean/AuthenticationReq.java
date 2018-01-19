package com.tarento.safetynet.bean;

public class AuthenticationReq {
	private String nonce;
	private String deviceId;
	private String attestation;

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAttestation() {
		return attestation;
	}

	public void setAttestation(String attestation) {
		this.attestation = attestation;
	}
}
