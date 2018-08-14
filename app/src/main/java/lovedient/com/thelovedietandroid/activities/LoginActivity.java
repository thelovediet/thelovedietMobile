package lovedient.com.thelovedietandroid.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.ActivityTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lovedient.com.thelovedietandroid.Constants;
import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.utils.SystemUtils;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{
    TextView login_activity_create_acount;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity=this;
        setXML();
    }
    public void setXML(){
        login_activity_create_acount = findViewById(R.id.login_activity_create_acount);
        login_activity_create_acount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_activity_create_acount){
            SystemUtils.startActivity(activity, RegisterActivity.class, Constants.CONTINUE_MODE);
        }
    }
}
