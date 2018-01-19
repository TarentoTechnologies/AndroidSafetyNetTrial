package com.example.tarento.executer;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.tarento.BaseApplication;
//import com.tarento.BaseApplication;

/**
 * singleton class for request queue
 */
public class VolleyRequestQueue {
    private static final String TAG = VolleyRequestQueue.class.getSimpleName();
    private static VolleyRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private VolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * get VolleyRequestQueue instance
     *
     * @return
     */
    public static synchronized VolleyRequestQueue getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(BaseApplication.getInstance());
        }
        return mInstance;
    }

    /**
     * get request queue instance
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * add new request in the queue
     *
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

}