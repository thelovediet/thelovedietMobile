package lovedient.com.thelovedietandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import lovedient.com.thelovedietandroid.Constants;

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
}
