package lovedient.com.thelovedietandroid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.model.UserModel;
import lovedient.com.thelovedietandroid.network.EndPoints;
import lovedient.com.thelovedietandroid.network.VollyInitilization;
import lovedient.com.thelovedietandroid.utils.Constants;
import lovedient.com.thelovedietandroid.utils.EditTextHelper;
import lovedient.com.thelovedietandroid.utils.SystemPref;
import lovedient.com.thelovedietandroid.utils.SystemUtils;
import lovedient.com.thelovedietandroid.utils.VolleyMultipartRequest;

public class RegisterActivity extends AppCompatActivity {

    EditText regitser_userName, register_email, register_password, register_retype_password,
            date, month, year;
    RadioGroup register_gendergroup, radio_group_pound, radio_group_time;
    RadioButton genderRadio, timeRadio, uniRadio;
    ProgressBar register_progressbar;
    CardView register_btn;
    Spinner country;
    RelativeLayout select_image;
    int GALLERY = 2001;
    int CAMERA = 2002;
    CircleImageView profile_image;
    private String IMAGE_DIRECTORY = "LoveDeit";
    private String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setXML();
    }

    public void setXML() {
        regitser_userName = findViewById(R.id.regitser_userName);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        select_image = findViewById(R.id.select_image);
        profile_image = findViewById(R.id.profile_image);
        register_retype_password = findViewById(R.id.register_retype_password);
        date = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        register_gendergroup = findViewById(R.id.register_gendergroup);
        radio_group_pound = findViewById(R.id.radio_group_pound);
        radio_group_time = findViewById(R.id.radio_group_time);
        country = findViewById(R.id.country);
        register_progressbar = findViewById(R.id.register_progressbar);
        register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFrom()) {
                    int genderId = register_gendergroup.getCheckedRadioButtonId();
                    int unitId = radio_group_pound.getCheckedRadioButtonId();
                    int timeId = radio_group_time.getCheckedRadioButtonId();
                    genderRadio = findViewById(genderId);
                    uniRadio = findViewById(unitId);
                    timeRadio = findViewById(timeId);
                    Bitmap bitmap = ((BitmapDrawable)profile_image.getDrawable()).getBitmap();
                    sendImage(bitmap);
                }
            }
        });
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }

    public boolean checkFrom() {
        EditTextHelper editTextHelper = new EditTextHelper();
        if (editTextHelper.isEmptyField(regitser_userName)) {
            editTextHelper.setError(regitser_userName, getResources().getString(
                    R.string.enter_name
            ));
            return false;
        }
        if (editTextHelper.isEmptyField(register_email)) {
            editTextHelper.setError(register_email, getResources().getString(
                    R.string.enter_email
            ));
            return false;
        }
        if (!editTextHelper.isEmailValid(register_email.getText().toString())) {
            editTextHelper.setError(register_email, getResources().getString(
                    R.string.enter_format
            ));
            return false;
        }
        if (editTextHelper.isEmptyField(register_password)) {
            editTextHelper.setError(register_password, getResources().getString(
                    R.string.enter_passowrd
            ));
            return false;
        }
        if (!register_password.getText().toString().
                equals(register_retype_password.getText().toString())) {
            editTextHelper.setError(register_password, getResources().getString(
                    R.string.password_not_match
            ));
            return false;
        }
        if (editTextHelper.isEmptyField(date)) {
            editTextHelper.setError(date, getResources().getString(
                    R.string.date_missing
            ));
            return false;
        }
        if (editTextHelper.isEmptyField(month)) {
            editTextHelper.setError(month, getResources().getString(
                    R.string.month_missing
            ));
            return false;
        }
        if (editTextHelper.isEmptyField(year)) {
            editTextHelper.setError(year, getResources().getString(
                    R.string.year_missing
            ));
            return false;
        }
        return true;
    }

    private void sendImage(final Bitmap bitmap) {
        final SystemPref pref = new SystemPref(getApplicationContext());
        register_progressbar.setVisibility(View.VISIBLE);
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST,
                EndPoints.REGISTER_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.v(TAG + "uploadImage", response.allHeaders.toArray().toString());
                        register_progressbar.setVisibility(View.GONE);
                        String data = new String(response.data);
                        try {
                            JSONObject dataJson = new JSONObject(data);
                            Log.v(TAG + "dataJson", dataJson.toString());
                            if(dataJson.has("status")){
                                if(dataJson.getBoolean("status")){
                                    UserModel userModel = new UserModel();
                                    JSONObject result = dataJson.getJSONObject("result");
                                    if(result.has("data")) {
                                        JSONObject dataObject = result.getJSONObject("data");
                                        userModel.setId(dataObject.getString("id"));
                                        userModel.setUserName(dataObject.getString("name"));
                                        userModel.setGender(dataObject.getString("gender"));
                                        userModel.setDateOfBirth(dataObject.getString("dob"));
                                        userModel.setCountry(dataObject.getString("country"));
                                        userModel.setImagePath(dataObject.getString("user_image"));
                                        userModel.setUnits(dataObject.getString("m_units"));
                                        userModel.setTimeFormate(dataObject.getString("time_format"));
                                        pref.setObjectData(Constants.USER_OBJECT, userModel);
                                        SystemUtils.showCustomToast(
                                                getResources().getString(R.string.user_register),RegisterActivity.this);
                                    }

                                }else{
                                    SystemUtils.showCustomToast(  getResources().getString(R.string.user_not_register),RegisterActivity.this);
                                }

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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put(Constants.NAME, regitser_userName.getText().toString());
                map.put(Constants.EMAIL, register_email.getText().toString());
                map.put(Constants.PASSWORD, register_password.getText().toString());
                map.put(Constants.GENDER, genderRadio.getText().toString());
                map.put(Constants.DATE_OF_BIRTH, date + "/" + month + "/" + year);
                map.put(Constants.COUNTRY, country.getSelectedItem().toString());
                map.put(Constants.UNITS, uniRadio.getText().toString());
                map.put(Constants.TIME_FORMATE, timeRadio.getText().toString());
                return map;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put(Constants.USER_IMAGE, new DataPart(Calendar.getInstance()
                        .getTimeInMillis() + "file_avatar.jpg", getFileDataFromDrawable(bitmap), "image/jpeg"));

                return params;
            }

        };
        VollyInitilization.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Convert image Bitmap to Bytes
     *
     * @param bitmap
     * @return
     */
    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    profile_image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profile_image.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(RegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
}
