package br.com.pauloceami.cursoappvendas.versaofinal.Json;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleySingleton {

    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;

    public VolleySingleton(Context ctx) {
        mRequestQueue = Volley.newRequestQueue(ctx);
    }

    public static VolleySingleton getmInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new VolleySingleton(ctx);
        }
        return mInstance;

    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }


}
