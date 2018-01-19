package com.example.tarento.model;


/**
 * getNonceApi response.
 */

public class GetNonceApiResponse extends BaseModel {

    private String nonce;

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}