package com.example.tarento.model;

import java.io.Serializable;

/**
 * GetNonceApi Request
 *
 * @author indraja machani
 */

public class GetNonceApiRequest implements Serializable {

    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
