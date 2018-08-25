package lovedient.com.thelovedietandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    CardView forgot_activity_btn;
    ProgressBar forgot_progressbar;
    TextView email_heading,done_heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setXML();
    }
    public void setXML(){
        forgot_activity_email = findViewById(R.id.forgot_activity_email);
        forgot_activity_btn = findViewById(R.id.forgot_activity_btn);
        email_heading = findViewById(R.id.email_heading);
        done_heading = findViewById(R.id.done_heading);
        forgot_progressbar = findViewById(R.id.forgot_progressbar);
        // apply fonts
        forgot_activity_email.setTypeface(SystemUtils.applyFont());
        email_heading.setTypeface(SystemUtils.applyFont());
        done_heading.setTypeface(SystemUtils.applyFont());

        forgot_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()) {
                    forgotPassword();
                }
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
                        SystemUtils.showCustomToast(SystemUtils.getActivity().getResources().
                                getString(R.string.email_send),ForgotPasswordActivity.this );
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
