package lovedient.com.thelovedietandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by MME on 6/23/2018.
 */

public class SystemPref {
    String TAG = SystemPref.class.getSimpleName();
   Context  context;
    SharedPreferences sharedPreferences;
    private String PREF_NAME="buzzshow";
    private String SAVE_OBJECT="saveObject";
    public SystemPref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }
    public void saveLogin(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_LOGIN,status);
        editor.commit();
    }
    public boolean  isLogin(){
        boolean login =  sharedPreferences.getBoolean(Constants.IS_LOGIN,false);
        return login;
    }
    public void setObjectData(String objectKey,Object object){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson json = new Gson();
        String jsonObject = json.toJson(object);
        Log.v(TAG+" OBJECT_TO_JSON","");
        editor.putString(objectKey,jsonObject);
        editor.commit();
    }
    public Object getOjectData(String objectKey,Class objectClass){
        String objectString  = sharedPreferences.getString(objectKey,"");
        Gson json = new Gson();
        Log.v(TAG+" OBJECT_TO_JSON",objectString);
        Object object = json.fromJson(objectString,objectClass);
        return object;
    }
    public void removeObject(String objectKey){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(objectKey);
        editor.commit();
    }
}
