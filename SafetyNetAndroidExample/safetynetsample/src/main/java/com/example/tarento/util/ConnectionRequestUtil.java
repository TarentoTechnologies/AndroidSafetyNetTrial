package com.example.tarento.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tarento.executer.api.BaseApi;
import com.example.tarento.executer.ErrorListener;
import com.example.tarento.executer.VolleyErrorHelper;
import com.example.tarento.executer.VolleyRequestQueue;
import com.example.tarento.model.BaseModel;
import com.example.tarento.ui.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Executing a network process..
 * <p/>
 */
public class ConnectionRequestUtil {
    private static final String TAG = ConnectionRequestUtil.class.getSimpleName();
    private static final long TIME_OUT = 30000;
    private boolean mIsBackground;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public ConnectionRequestUtil(Context context) {
        this.mContext = context;
    }

    /**
     * create a progress dialog
     */
    private void createProgressDialog() {
        if (mIsBackground) {
            return;
        }

        Utility.showProgressDialog(mContext);

        if (mIsBackground) {
            return;
        }

        mProgressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.app_name),
                mContext.getString(R.string.alert__lbl__please_wait));
    }

    /**
     * dismiss progress dialog
     */
    private void dismissProgressDialog() {
        if (mIsBackground) {
            return;
        }

        Utility.dismissProgressDialog();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = null;
    }

    /**
     * create and execute a json request
     *
     * @param api
     * @param responseListener
     * @param errorListener
     */
    public void createJsonRequest(final BaseApi api, final Response.Listener responseListener, final ErrorListener errorListener) {

        //create a loader
        createProgressDialog();

        if (Constant.DEBUG) {
            Log.d(TAG, "Request url: " + api.getUrl());
            Log.d(TAG, "Request method: " + api.getMethod());
            Log.d(TAG, "Request body: " + api.getBody());
        }

        //create a request
        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(api.getMethod(), api.getUrl(), api.getBody(),
                        responseListener, errorListener) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = getCommonHeaders();
                        Map<String, String> apiHeaders = api.getHeaders();
                        if (apiHeaders != null) {
                            headers.putAll(apiHeaders);
                        }

                        if (Constant.DEBUG) {
                            for (String key : headers.keySet()) {
                                Log.d(TAG, "Header Key: " + key + ", Value: " + headers.get(key));
                            }
                        }
                        return headers;
                    }

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser
                                    .parseCharset(response.headers, PROTOCOL_CHARSET));
                            if (Constant.DEBUG) {
                                Log.d(TAG, "Response data:" + jsonString);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        //dismiss loader
                        dismissProgressDialog();
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        return super.parseNetworkError(volleyError);
                    }

                    @Override
                    public void deliverError(VolleyError error) {
                        super.deliverError(error);
                        String errorMessage =
                                VolleyErrorHelper.getInstance(mContext).getMessage(error);
                        if (!TextUtils.isEmpty(errorMessage)) {
                            Log.d(TAG, "Error message: " + errorMessage);
                        }
                        //dismiss loader
                        dismissProgressDialog();
                    }

                    @Override
                    protected void deliverResponse(JSONObject response) {
                        Log.d(TAG, "response  : " + response.toString());
                        BaseModel baseModel = new BaseModel();
                        try {
                            baseModel = api.serialize(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (baseModel.isResponseStatusSuccess()) {
                            if (responseListener != null) {
                                responseListener.onResponse(baseModel);
                            }
                        } else {
                            if (errorListener != null) {
                                errorListener.onErrorResponse(baseModel);
                            }
                        }
                    }
                };

        //default retry policy
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy((int) TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //cache the response data for a url
        jsonObjReq.setShouldCache(api.isShouldCache());

        //add to request queue
        VolleyRequestQueue.getInstance().addToRequestQueue(jsonObjReq, "" + api.getRequestId());

    }

      /**
     * get common headers for all api
     *
     * @return
     */
    private HashMap<String, String> getCommonHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(Constant.CONTENT_TYPE, Constant.CONTENT_TYPE_JSON);

        //TODO: if required Add common headers
        // set auth_token header
        //        String authToken = DBUtil.getInstance().getAuthToken();
        //        if (!TextUtils.isEmpty(authToken)) {
        //            headers.put(ApiRequestKey.KEY_AUTH_TOKEN, authToken);
        //        }
        return headers;
    }


    /**
     * Run in background and no progress loader
     *
     * @param mIsBackground
     */
    public void setRunInBackground(boolean mIsBackground) {
        this.mIsBackground = mIsBackground;
    }
}
