package com.example.tarento.executer;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.tarento.model.BaseModel;

/**
 * ErrorListener
 *
 * @author Indraja Machani
 */
public interface ErrorListener extends Response.ErrorListener {

    @Override
    void onErrorResponse(VolleyError error);

    void onErrorResponse(BaseModel baseModel);
}
