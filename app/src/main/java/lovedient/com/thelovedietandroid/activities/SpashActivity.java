package lovedient.com.thelovedietandroid.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.utils.SystemPref;
import lovedient.com.thelovedietandroid.utils.SystemUtils;

public class SpashActivity extends AppCompatActivity {
    private final int TIME_OUT=2000;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_spash);
        setContentView(R.layout.new_registration_layout);
        SystemUtils.setActivity(this);
        SystemPref pref = new SystemPref(getApplicationContext());
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                       // txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
      /*  if (pref.isLogin()) {
            startActivity(new Intent(getApplicationContext(), MainPointActivity.class));
        } else {
            spashTime();
        }*/
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
