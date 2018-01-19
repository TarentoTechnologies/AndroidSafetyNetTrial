package com.example.tarento.model;


/**
 * validateDeviceApi Response
 *
 * @author indraja machani
 */

public class ValidateDeviceApiResponse extends BaseModel {

    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}