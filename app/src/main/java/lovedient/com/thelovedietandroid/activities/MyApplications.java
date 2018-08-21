package lovedient.com.thelovedietandroid.activities;

import android.app.Application;

public class MyApplications  extends Application{
    public static boolean status;
    public static boolean isActivityVisible(){
        return status;
    }
    public static void setActivityResume(){
        status = true;
    }
    public static void setActivityPause(){
        status = false;
    }
}
