package com.example.tarento.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings;
import android.view.WindowManager;

import com.example.tarento.BaseApplication;
import com.example.tarento.ui.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 */
public class Utility {
    private static ProgressDialog progressDialog;

    /**
     * get android device id
     *
     * @return
     */
    public static String getDeviceId() {
        String deviceId = Settings.Secure.getString(BaseApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    // build a GSON
    private static Gson gson = new GsonBuilder().create();

    /**
     * getting a GSON object instance
     *
     * @return
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * @param src
     * @param clazz
     * @return
     */
    public static <T> String toJson(T src, Class<T> clazz) {
        return gson.toJson(src, clazz);
    }

    /**
     * To show the progress dialog with the title as application name and message as "Loading" text.
     *
     * @param context
     */
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            try {
                progressDialog.show();
            } catch (WindowManager.BadTokenException e) {

            }
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.progress_dialog_layout);
        }

    }

    /**
     * To dismiss the progress dialog
     */
    public static void dismissProgressDialog() {
        // try catch block for handling java.lang.IllegalArgumentException
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
        progressDialog = null;
    }


}
