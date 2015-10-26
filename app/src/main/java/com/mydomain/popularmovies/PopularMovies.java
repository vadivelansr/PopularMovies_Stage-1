package com.mydomain.popularmovies;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by vadivelansr on 10/20/2015.
 */
public class PopularMovies extends Application {
    public static final String TAG = PopularMovies.class.getSimpleName();
    private RequestQueue requestQueue;
    private static PopularMovies instance;
    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }
    public static synchronized PopularMovies getInstance(){
        return instance;
    }
    public  RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(String tag){
        if(requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }

}
