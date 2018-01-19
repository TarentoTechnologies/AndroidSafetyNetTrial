package com.tarento.safetynet.bean;

import java.util.Arrays;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.util.Key;

public class Attestation extends JsonWebSignature.Payload {

	@Key
	private String nonce;

	@Key
	private long timestampMs;

	@Key
	private String apkPackageName;

	@Key
	private String[] apkCertificateDigestSha256;

	@Key
	private String apkDigestSha256;

	@Key
	private boolean ctsProfileMatch;

	@Key
	private boolean basicIntegrity;

	public String getNonce() {
		return nonce;
	}

	public long getTimestampMs() {
		return timestampMs;
	}

	public String getApkPackageName() {
		return apkPackageName;
	}

	public String getApkDigestSha256() {
		return apkDigestSha256;
	}

	public String[] getApkCertificateDigestSha256() {
		return apkCertificateDigestSha256;
	}

	public boolean isCtsProfileMatch() {
		return ctsProfileMatch;
	}

	public boolean hasBasicIntegrity() {
		return basicIntegrity;
	}

	@Override
	public String toString() {
		return "Attestation [nonce=" + nonce + ", timestampMs=" + timestampMs + ", apkPackageName=" + apkPackageName
				+ ", apkCertificateDigestSha256=" + Arrays.toString(apkCertificateDigestSha256) + ", apkDigestSha256="
				+ apkDigestSha256 + ", ctsProfileMatch=" + ctsProfileMatch + ", basicIntegrity=" + basicIntegrity + "]";
	}
	
	
}