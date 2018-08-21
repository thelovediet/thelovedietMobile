package lovedient.com.thelovedietandroid.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VollyInitilization {
    private Context context;
    private RequestQueue requestQueue;
    private static final String TAG = VollyInitilization.class.getSimpleName();
    private static VollyInitilization mInstance;
    private VollyInitilization(Context context){
        this.context = context;
        this.requestQueue = getRequestQueue();
    }
    public static synchronized VollyInitilization getInstance(Context context){
        if(mInstance==null){
            mInstance = new VollyInitilization(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return  requestQueue;
    }
    public <T> void addRequest(Request<T> request){
        requestQueue.add(request);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }
}
