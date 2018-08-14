package lovedient.com.thelovedietandroid.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.utils.SystemUtils;

public class SpashActivity extends AppCompatActivity {
    private final int TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        SystemUtils.setActivity(this);
        spashTime();
    }
    public void spashTime(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        }, TIME_OUT);
    }
}
