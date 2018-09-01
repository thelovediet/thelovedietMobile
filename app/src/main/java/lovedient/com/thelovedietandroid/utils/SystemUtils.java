package lovedient.com.thelovedietandroid.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.model.UserModel;

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
    public static void showErrorMessage(String message,Activity activity){
            final Dialog view = new Dialog(activity);
        view.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view.setCancelable(false);
        view.setContentView(R.layout.message_popup);
        TextView msg = view.findViewById(R.id.message);
        ImageView close = view.findViewById(R.id.close);
        msg.setText(message);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.dismiss();
            }
        });
        view.show();
    }
    public static void showMessageType(String message,Activity activity,String type){
            final Dialog view = new Dialog(activity);
        view.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view.setCancelable(false);
        view.setContentView(R.layout.message_popup);
        TextView msg = view.findViewById(R.id.message);
        TextView error = view.findViewById(R.id.error);
        error.setText(type);
        ImageView close = view.findViewById(R.id.close);
        msg.setText(message);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.dismiss();
            }
        });
        view.show();
    }
    public static UserModel getUserStatus(Context context){
        SystemPref systemPref = new SystemPref(context);
        UserModel userStatus = (UserModel) systemPref.getOjectData(Constants.USER_OBJECT,UserModel.class);
        return userStatus;
    }
    public static Typeface applyFont(){
        Typeface font =  Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/CHERL___.TTF");
        return  font;
    }
    public static Typeface didnoFont(){
        Typeface font =  Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Didno.ttf");
        return  font;
    } public static Typeface robotoFont(){
        Typeface font =  Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto.ttf");
        return  font;
    }
    public static  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
