/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.example.tarento.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.tarento.executer.api.GetNonceApi;
import com.example.tarento.executer.api.ValidateDeviceApi;
import com.example.tarento.model.GetNonceApiRequest;
import com.example.tarento.model.GetNonceApiResponse;
import com.example.tarento.model.ValidateDeviceApiRequest;
import com.example.tarento.model.ValidateDeviceApiResponse;
import com.example.tarento.util.ConnectionRequestUtil;
import com.example.tarento.executer.ErrorListener;
import com.example.tarento.model.BaseModel;
import com.example.tarento.util.Utility;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.safetynet.SafetyNetClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple launcher activity containing a summary sample description
 * and a few action bar buttons.
 */
public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
    private String mDeviceId;
    private String mResult;
    private String mNonce;
    /**
     * get nonce api success listener
     */
    private Response.Listener getNonceApiSuccessListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                handleGetNonceApi((GetNonceApiResponse) response);
            }
        }
    };

    /**
     * get nonce api success listener
     */
    private Response.Listener validateDeviceApiSuccessListener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            if (response != null) {
                handleValidateDeviceApi((ValidateDeviceApiResponse) response);
            }
        }
    };

    /**
     * api error listener
     */
    private ErrorListener errorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }

        @Override
        public void onErrorResponse(BaseModel baseModel) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDeviceId = Utility.getDeviceId();


        // verify device by button click
        findViewById(R.id.btn_verify_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGetNounceApi();
            }
        });
    }

    /**
     * handle GetNonce api response
     */
    private void handleGetNonceApi(final GetNonceApiResponse getNonceApiResponse) {
        Utility.dismissProgressDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getNonceApiResponse != null) {
                    // get nonce
                    mNonce = getNonceApiResponse.getNonce();
                    Log.d(TAG, "nonceData " + mNonce);

        /*
         Call the SafetyNet API asynchronously.
         The result is returned through the success or failure listeners.
         First, get a SafetyNetClient for the foreground Activity.
         Next, make the call to the attestation API. The API key is specified in the gradle build
         configuration and read from the gradle.properties file.
         */
                    if (mNonce != null && !mNonce.isEmpty()) {
                        SafetyNetClient client = SafetyNet.getClient(MainActivity.this);
                        Task<SafetyNetApi.AttestationResponse> task = client.attest(mNonce.getBytes(), BuildConfig.SAFETYNET_API_KEY);

                        task.addOnSuccessListener(MainActivity.this, mSafeTyNetApiSuccessListener)
                                .addOnFailureListener(MainActivity.this,
                                        mSafeTyNetApiFailureListener);
                    }
                }
            }
        });
    }

    /**
     * start GetNonce api
     */
    private void startGetNounceApi() {
        Log.d(TAG, "startGetNounceApi");
        GetNonceApiRequest getNonceApiRequestModel = new GetNonceApiRequest();
        getNonceApiRequestModel.setDeviceId(mDeviceId);

        GetNonceApi api = new GetNonceApi(getNonceApiRequestModel);
        ConnectionRequestUtil requestUtil = new ConnectionRequestUtil(this);
        requestUtil.createJsonRequest(api, getNonceApiSuccessListener, errorListener);
    }

    /**
     * Called after successfully communicating with the SafetyNet API.
     * The #onSuccess callback receives an
     * {@link com.google.android.gms.safetynet.SafetyNetApi.AttestationResponse} that contains a
     * JwsResult with the attestation result.
     */
    private OnSuccessListener<SafetyNetApi.AttestationResponse> mSafeTyNetApiSuccessListener =
            new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
                @Override
                public void onSuccess(SafetyNetApi.AttestationResponse attestationResponse) {
                    /*
                     Successfully communicated with SafetyNet API.
                     Use result.getJwsResult() to get the signed result data. See the server
                     component of this sample for details on how to verify and parse this result.
                     */
                    mResult = attestationResponse.getJwsResult();
                    Log.d(TAG, "Success! SafetyNet result:\n" + mResult + "\n");

                        /*
                         TODO(developer): Forward this result to your server together with
                         the nonce for verification.
                         You can also parse the JwsResult locally to confirm that the API
                         returned a response by checking for an 'error' field first and before
                         retrying the request with an exponential backoff.

                         NOTE: Do NOT rely on a local, client-side only check for security, you
                         must verify the response on a remote server!
                        */
                    startValidateDeviceApi();
                }
            };

    /**
     * Called when an error occurred when communicating with the SafetyNet API.
     */
    private OnFailureListener mSafeTyNetApiFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // An error occurred while communicating with the service.
            mResult = null;

            if (e instanceof ApiException) {
                // An error with the Google Play Services API contains some additional details.
                ApiException apiException = (ApiException) e;
                Log.d(TAG, "Error: " +
                        CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " +
                        apiException.getStatusMessage());
            } else {
                // A different, unknown type of error occurred.
                Log.d(TAG, "ERROR! " + e.getMessage());
            }

        }
    };


    /**
     * start Validate device api
     */
    private void startValidateDeviceApi() {
        Log.d(TAG, "startValidateDeviceApi");
        ValidateDeviceApiRequest validateDeviceApiRequest = new ValidateDeviceApiRequest();
        validateDeviceApiRequest.setDeviceId(mDeviceId);
        validateDeviceApiRequest.setNonce(mNonce);
        validateDeviceApiRequest.setAttestation(mResult);

        ValidateDeviceApi api = new ValidateDeviceApi(validateDeviceApiRequest);
        ConnectionRequestUtil requestUtil = new ConnectionRequestUtil(this);
        requestUtil.createJsonRequest(api, validateDeviceApiSuccessListener, errorListener);
    }


    /**
     * handleValidateDeviceApi response
     */
    private void handleValidateDeviceApi(final ValidateDeviceApiResponse response) {
        Utility.dismissProgressDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response != null) {
                    // get nonce
                    if (response.isValid()) {
                        Toast.makeText(MainActivity.this, "Device verified successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to verify device", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
