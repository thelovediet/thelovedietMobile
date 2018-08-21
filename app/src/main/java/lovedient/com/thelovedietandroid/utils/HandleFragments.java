package lovedient.com.thelovedietandroid.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import lovedient.com.thelovedietandroid.R;

public class HandleFragments {
    public static void startTansaction(Activity context, Fragment object,String title,int  layout){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(layout, object)
                .commit();
    }

    public static void goTofragment(Activity context, Fragment object,String title,int  layout){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
               // .setCustomAnimations(R.anim.slider_up,R.anim.slider_out)
                .replace(R.id.container,object).addToBackStack(null).commit();
    }

    public static void goBackScreen(Activity context, Fragment object,String title,int  layout){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slider_up,R.anim.slider_out)
                .replace(layout, object)
                .commit();
    }
    public static void addFragment(Activity context, Fragment object,String title,int  layout){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slider_up,R.anim.slider_out)
                .add(layout, object)
                .commit();
    }
    public static void removeActiveCenterFragments(Activity context) {
        context.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
