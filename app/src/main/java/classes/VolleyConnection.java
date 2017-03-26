package classes;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ebada on 01/03/2017.
 */

public class VolleyConnection {
    private static VolleyConnection sInsteance = null;
    private RequestQueue mRequestQueue;
    private VolleyConnection() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppConetext());
    }

    public static VolleyConnection getsInsteance() {
        if (sInsteance == null) {
            sInsteance = new VolleyConnection();
        }
            return sInsteance;
    }

    public RequestQueue getmRequestQueue()
    {
        return mRequestQueue;
    }

    public <T> void addToRequestQueue (Request<T> request)
    {
        mRequestQueue.add(request);
    }
}

