package lovedient.com.thelovedietandroid.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by MME on 5/28/2018.
 */

public class EditTextHelper {
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public boolean isEmailValid(CharSequence charSequence){
        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }
    public boolean isEmptyField(EditText editText){
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }
    public void setError(EditText editText,String message){
        editText.setError(message);
    }
}
