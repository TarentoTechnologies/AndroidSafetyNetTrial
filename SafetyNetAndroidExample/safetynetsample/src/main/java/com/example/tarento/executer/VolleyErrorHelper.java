package com.example.tarento.executer;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.tarento.ui.R;
import com.example.tarento.util.Constant;
import com.google.gson.JsonSyntaxException;

public class VolleyErrorHelper {

    private static final String TAG = VolleyErrorHelper.class.getSimpleName();
    private static VolleyErrorHelper volleyErrorHelper = null;
    private Context mContext;

    private VolleyErrorHelper(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @return
     */
    public static synchronized VolleyErrorHelper getInstance(Context mContext) {
        if (volleyErrorHelper == null) {
            volleyErrorHelper = new VolleyErrorHelper(mContext);
        }
        return volleyErrorHelper;
    }

    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @return
     */
    public String getMessage(Object error) {
        getErrorCode(error);
        String errorMessage = null;
        if (error instanceof TimeoutError) {
            errorMessage = mContext.getResources().getString(R.string.alert_lbl_the_server_down);
        } else if (isServerProblem(error)) {
            errorMessage = handleServerError(error, mContext);
        } else if (isNetworkProblem(error)) {
            errorMessage = mContext.getResources()
                    .getString(R.string.alert_lbl_no_internet_connectivity);
        } else {
            errorMessage = mContext.getResources().getString(R.string.alert_lbl_the_server_down);
        }
        showAlertDialog(errorMessage);
        return errorMessage;
    }

    /**
     * get connection error code
     *
     * @param error
     */
    private int getErrorCode(Object error) {
        VolleyError volleyError = (VolleyError) error;
        NetworkResponse response = volleyError.networkResponse;
        int errorCode = 0;
        if (response != null) {
            errorCode = response.statusCode;
            if (Constant.DEBUG) {
                Log.d(TAG, "Error Code: " + errorCode);
            }
        }
        return errorCode;
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param error
     * @param context
     * @return
     */
    private String handleServerError(Object error, Context context) {
        int errorCode = getErrorCode(error);
        VolleyError volleyError = (VolleyError) error;
        NetworkResponse response = volleyError.networkResponse;
        if (response != null && errorCode > 0) {
            switch (errorCode) {
                case 404:
                case 400:
                case 422:
                case 401:

                    String errorMessage = new String(response.data);
                    // find a error message from json
                    try {
                        // TODO enable if required
                        //                        if (Constants.DEBUG) {
                        //                            if (!TextUtils.isEmpty(errorMessage)) {
                        //                                Log.d(TAG, "Error response: " + errorMessage);
                        //                            }
                        //                        }
                        //                        ErrorResponse errorResponse = GsonUtil.getGson().fromJson(errorMessage,
                        //                                ErrorResponse.class);
                        //                        if (errorResponse != null) {
                        //                            errorMessage = errorResponse.getErrorMessage();
                        //                        }
                        //
                        //                        if (errorCode == 401 && errorMessage == null) {
                        //                            errorMessage = mContext
                        //                                    .getString(R.string.alert__lbl__your_session_has_expired_please_login_again_);
                        //                        }
                        return errorMessage;
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }

                    // invalid request
                    return volleyError.getMessage();

                default:
                    return context.getResources().getString(R.string.alert_lbl_the_server_down);
            }
        }
        return context.getResources().getString(R.string.alert_lbl_the_server_down);
    }


    /**
     * show error message dialog
     *
     * @param errorMessage
     */
    private void showAlertDialog(final String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) {
            return;
        }

        //show toast
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();

    }
}