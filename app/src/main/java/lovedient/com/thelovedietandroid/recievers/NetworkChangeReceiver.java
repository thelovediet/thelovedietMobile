package lovedient.com.thelovedietandroid.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MME on 7/18/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Send Broadcast to MainActivity
        context.sendBroadcast(new Intent("INTERNET_LOST"));
    }
}
