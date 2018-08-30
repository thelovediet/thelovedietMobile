package lovedient.com.thelovedietandroid.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
import lovedient.com.thelovedietandroid.utils.HandleDialog;
import lovedient.com.thelovedietandroid.utils.MySpinnerAdapter;
import lovedient.com.thelovedietandroid.utils.SystemPref;
import lovedient.com.thelovedietandroid.utils.SystemUtils;
import lovedient.com.thelovedietandroid.utils.VolleyMultipartRequest;

public class NewRegisterActivity  extends AppCompatActivity {
    private EditText register_useName,registerEmail,registerRetypeEmail,
    registerPassowrd,registerRePassowrd,register_birthDate;
    private ImageView  userImage,already_register,back;
    private CardView regitser_take_picture;
    private Spinner registerGender,registerCountry,registerUnits,registerTimeFormat;
    private CheckBox register_term_of_services;
    Button register_btn;
    private final String TAG = NewRegisterActivity.class.getSimpleName();
    int GALLERY = 2001;
    int CAMERA = 2002;
    CircleImageView profile_image;
    ProgressBar register_progressbar;
    private String IMAGE_DIRECTORY = "LoveDeit";
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_registration_layout);
        setXML();
    }
    public void setXML(){
        back = findViewById(R.id.back);
        register_useName = findViewById(R.id.register_userName);
        registerEmail = findViewById(R.id.register_email);
        registerRetypeEmail = findViewById(R.id.register_retype_email);
        registerPassowrd = findViewById(R.id.register_password);
        registerRePassowrd = findViewById(R.id.register_repassword);
        regitser_take_picture = findViewById(R.id.register_take_picture);
        profile_image = findViewById(R.id.user_camera_image);
        registerGender = findViewById(R.id.register_genders);
        register_birthDate = findViewById(R.id.register_birthDate);
        registerCountry = findViewById(R.id.register_country);
        registerUnits = findViewById(R.id.register_units);
        registerTimeFormat = findViewById(R.id.register_time_format);
        register_term_of_services = findViewById(R.id.register_term_of_services);
        register_btn = findViewById(R.id.register_btn);
        already_register = findViewById(R.id.already_register);
        register_progressbar = findViewById(R.id.register_progressbar);
        setActions();
    }
    public void setActions(){

        MySpinnerAdapter adapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.country_spinner_layout,
                Arrays.asList(getResources().getStringArray(R.array.country_arrays))
        );
        MySpinnerAdapter unitAdapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.country_spinner_layout,
                Arrays.asList(getResources().getStringArray(R.array.units))
        );
        MySpinnerAdapter timeAdapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.country_spinner_layout,
                Arrays.asList(getResources().getStringArray(R.array.time))
        );
        MySpinnerAdapter genderAdapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.country_spinner_layout,
                Arrays.asList(getResources().getStringArray(R.array.time))
        );
        registerCountry.setAdapter(adapter);
        registerUnits.setAdapter(unitAdapter);
        registerTimeFormat.setAdapter(timeAdapter);
        registerGender.setAdapter(genderAdapter);
        register_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateBirth();
            }
        });
        regitser_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()){
                    Bitmap bitmap = ((BitmapDrawable)profile_image.getDrawable()).getBitmap();
                    sendImage(bitmap);
                }else{
                    SystemUtils.showErrorMessage(getResources().getString(R.string.please_fill),
                            NewRegisterActivity.this);
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean checkForm(){
        EditTextHelper editTextHelper = new EditTextHelper();
        if(editTextHelper.isEmptyField(register_useName)){
            editTextHelper.setError(register_useName, getResources().getString(R.string.fill_field));
            return false;
        }
        if(editTextHelper.isEmptyField(registerEmail)) {
            editTextHelper.setError(registerEmail, getResources().getString(R.string.fill_field));
            return false;
        }
        if(!editTextHelper.isEmailValid(registerEmail.getText().toString().trim())){
            editTextHelper.setError(registerEmail, getResources().getString(R.string.enter_format));
            return false;
        }
        if(!editTextHelper.isEmailValid(registerRetypeEmail.getText().toString().trim())){
            editTextHelper.setError(registerRetypeEmail, getResources().getString(R.string.enter_format));
            return false;
        }
        if(editTextHelper.isEmptyField(registerRetypeEmail)) {
            editTextHelper.setError(registerRetypeEmail, getResources().getString(R.string.fill_field));
            return false;
        }
        if(editTextHelper.isEmptyField(register_birthDate)) {
            editTextHelper.setError(register_birthDate, getResources().getString(R.string.fill_field));
            return false;
        }
        if(!registerEmail.getText().toString().trim().equals(registerRetypeEmail.getText().toString().trim())){
            editTextHelper.setError(registerRetypeEmail, getResources().getString(R.string.email_not_match));
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
                                                getResources().getString(R.string.user_created),NewRegisterActivity.this);
                                        finish();
                                    }

                                }else{
                                    SystemUtils.showErrorMessage( getResources().getString(R.string.invalid_email_password),
                                            NewRegisterActivity.this);
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
                map.put(Constants.NAME, register_useName.getText().toString());
                map.put(Constants.EMAIL, registerEmail.getText().toString());
                map.put(Constants.PASSWORD, registerPassowrd.getText().toString());
                map.put(Constants.GENDER, registerGender.getSelectedItem().toString());
                map.put(Constants.DATE_OF_BIRTH,register_birthDate.getText().toString());
                map.put(Constants.COUNTRY, registerCountry.getSelectedItem().toString());
                map.put(Constants.UNITS, registerUnits.getSelectedItem().toString());
                map.put(Constants.TIME_FORMATE,registerTimeFormat.getSelectedItem().toString());
                map.put(Constants.TYPE, Constants.TYPE_NORMAL);
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
                    Toast.makeText(NewRegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    profile_image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NewRegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profile_image.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(NewRegisterActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
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
    public void selectDateBirth(){
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

                       register_birthDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}
