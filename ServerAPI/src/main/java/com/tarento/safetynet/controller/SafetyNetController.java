package com.tarento.safetynet.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLException;

import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.util.Base64;
import com.tarento.safetynet.bean.Attestation;
import com.tarento.safetynet.bean.AuthenticationReq;
import com.tarento.safetynet.bean.AuthenticationResponse;
import com.tarento.safetynet.bean.NonceReq;
import com.tarento.safetynet.bean.NonceResponse;

@RestController
public class SafetyNetController {

	private static final DefaultHostnameVerifier DOMAINNAME_VERIFIER = new DefaultHostnameVerifier();
	
	@Autowired
	private Environment env;

	/* 
	 * This should ideally be a persistent store. Keeping it in memory for testing alone
	 */
	private Map<String, String> deviceNonceStore = new HashMap<>();

	@RequestMapping(value = "/getNonce", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
			"application/json" })
	public NonceResponse getNonce(@RequestBody NonceReq device) {
		String uuid = UUID.randomUUID().toString();
		deviceNonceStore.put(device.getDeviceId(), uuid);
		System.out.println("Nonce" + uuid);
		NonceResponse response = new NonceResponse(uuid);
		response.setResponseStatus(true);
		return response;
	}

	@RequestMapping(value = "/validateDevice", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	public AuthenticationResponse validateDevice(@RequestBody AuthenticationReq authReq) {
		boolean authResult = false;

		String nonceGivenToDevice = deviceNonceStore.get(authReq.getDeviceId());
		if (nonceGivenToDevice != null && nonceGivenToDevice.equals(authReq.getNonce())) {
			JsonWebSignature jws = parseAttestation(authReq.getAttestation());
			if (jws != null && verifyAttestation(jws) && verifyPayload(jws, nonceGivenToDevice)) {
					authResult = true;
			}
		}
		AuthenticationResponse response = new AuthenticationResponse(authResult);
		response.setResponseStatus(true);
		return response;
	}

	private boolean verifyPayload(JsonWebSignature jws, String nonceGivenToDevice) {
		Attestation attestation = (Attestation) jws.getPayload();
		String attestedNonce = new String(Base64.decodeBase64(attestation.getNonce()));
		long currentTime = System.currentTimeMillis();

		/* 
		 * Checking that the timestamp is not very old and also that the apkSHA1 is one we have published. 
		 * Make it more secure by also
		 */
		if (attestation.isCtsProfileMatch() && 
			attestation.hasBasicIntegrity() && 
			((currentTime - attestation.getTimestampMs()) < 120000) &&
			nonceGivenToDevice.equals(attestedNonce) &&
			isKnownValue("validAPKs", attestation.getApkDigestSha256()) && 
			isKnownValue("validCerts", attestation.getApkCertificateDigestSha256()[0]) &&
			isKnownValue("packageName", attestation.getApkPackageName())) {
				System.out.println("valid");	
				return true;
		}
		return false;
	}

	private boolean verifyAttestation(JsonWebSignature jws) {
		boolean verificationResult = false;
		X509Certificate cert;
		try {
			cert = jws.verifySignature();
			//Need to see if the cert needs to be revalidated apart from checking the hostname in the certificate is that of Google
			if (cert != null) {
				DOMAINNAME_VERIFIER.verify("attest.android.com", cert);
				verificationResult = true;
			}
		} catch (GeneralSecurityException | SSLException e) {
			//Any exception implies that the signature verification failed
		}
		
		return verificationResult;
	}

	private JsonWebSignature parseAttestation(String attestation) {
		JsonWebSignature jws = null;
		try {
			jws = JsonWebSignature.parser(JacksonFactory.getDefaultInstance()).setPayloadClass(Attestation.class)
					.parse(attestation);
		} catch (IOException e) {
		}
		return jws;
	}
	
	private boolean isKnownValue(String propertyName, String value) {
		String knownValues = env.getProperty(propertyName);
		System.out.println("Recd value" + knownValues);
		return knownValues.indexOf(value) != -1;
	}

}
