package lovedient.com.thelovedietandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MME on 7/18/2018.
 */

public class ServiceManager {
    Context context;
    public ServiceManager(Context context){
        this.context  = context;
    }
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  connectivityManager.getActiveNetworkInfo();
        return info!=null && info.isConnected();
        }
    }

