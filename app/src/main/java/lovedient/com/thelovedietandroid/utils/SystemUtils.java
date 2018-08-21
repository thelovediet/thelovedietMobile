package lovedient.com.thelovedietandroid.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import lovedient.com.thelovedietandroid.R;

public class SystemUtils {
    public static Activity  activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        SystemUtils.activity = activity;
    }

    public static void startActivity(Activity context, Class  className, int  mode){
        activity.startActivity(new Intent(context,className));
        if(mode== Constants.FINISH_MODE){
            activity.finish();
        }
    }
    /**
     * Show Custom Toast Message
     * @param message
     * @param activity
     */

    public static void showCustomToast(String message,Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                null);
        TextView msg = (TextView) layout.findViewById(R.id.toast_message);
        msg.setText(message);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
