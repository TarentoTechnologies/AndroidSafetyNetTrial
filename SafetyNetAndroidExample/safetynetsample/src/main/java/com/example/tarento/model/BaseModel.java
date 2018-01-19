package com.example.tarento.model;

import java.io.Serializable;

/**
 * BaseModel
 *
 * @author Indraja Machani
 */
public class BaseModel implements Serializable {

    private boolean responseStatus;

    public boolean isResponseStatusSuccess() {
        return responseStatus;
    }

    public void setResponseStatus(boolean responseStatus) {
        this.responseStatus = responseStatus;
    }
}
