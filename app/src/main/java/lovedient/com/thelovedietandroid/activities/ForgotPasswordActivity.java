package lovedient.com.thelovedietandroid.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.network.EndPoints;
import lovedient.com.thelovedietandroid.network.VollyInitilization;
import lovedient.com.thelovedietandroid.utils.Constants;
import lovedient.com.thelovedietandroid.utils.EditTextHelper;
import lovedient.com.thelovedietandroid.utils.SystemUtils;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG =  ForgotPasswordActivity.class.getSimpleName();
    EditText forgot_activity_email;
    Button forgot_activity_btn;
    ProgressBar forgot_progressbar;
    String success= "Email Has been sended you please check your inbox or spam.";
    String error= "Invalid Email. No user found against this user";
    ImageView register_account,already_account,back;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_forgot);
        activity = this;
        setXML();
    }
    public void setXML(){
        forgot_activity_email = findViewById(R.id.forgot_activity_email);
        forgot_activity_btn = findViewById(R.id.forgot_activity_btn);
        forgot_progressbar = findViewById(R.id.forgot_progressbar);
        register_account = findViewById(R.id.register_account);
        already_account = findViewById(R.id.already_account);
        back = findViewById(R.id.back);
        // apply fonts
        forgot_activity_email.setTypeface(SystemUtils.robotoFont());
        forgot_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()) {
                    forgotPassword();
                }
            }
        });
        // Back Press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtils.startActivity(activity, NewRegisterActivity.class, Constants.FINISH_MODE);
            }
        });
        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtils.startActivity(activity, LoginActivity.class, Constants.FINISH_MODE);
            }
        });
    }
    public void forgotPassword(){
        forgot_progressbar.setVisibility(View.VISIBLE);
        StringRequest request  = new StringRequest(Request.Method.POST, EndPoints.FORGOT_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                forgot_progressbar.setVisibility(View.GONE);
                Log.v(TAG+" forgotPassword ",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        if(jsonObject.getString("message").equalsIgnoreCase(success)){
                        SystemUtils.showMessageType(jsonObject.getString("message"),
                                ForgotPasswordActivity.this ,"SUCCESS");
                            forgot_activity_email.setText("");
                        }
                        if(jsonObject.getString("message").equalsIgnoreCase(error)){
                        SystemUtils.showMessageType(jsonObject.getString("message"),
                                ForgotPasswordActivity.this ,"ERROR");}
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put(Constants.EMAIL, forgot_activity_email.getText().toString());
                return map;
            }
        };
        VollyInitilization.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
    public boolean checkForm() {
        EditTextHelper editTextHelper = new EditTextHelper();

        if (editTextHelper.isEmptyField(forgot_activity_email)) {
            editTextHelper.setError(forgot_activity_email, getResources().getString(
                    R.string.enter_email
            ));
            return false;
        }
        if (!editTextHelper.isEmailValid(forgot_activity_email.getText().toString())) {
            editTextHelper.setError(forgot_activity_email, getResources().getString(
                    R.string.enter_format
            ));
            return false;
        }
        return true;
    }
}
