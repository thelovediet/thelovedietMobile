package lovedient.com.thelovedietandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lovedient.com.thelovedietandroid.model.UserModel;
import lovedient.com.thelovedietandroid.network.EndPoints;
import lovedient.com.thelovedietandroid.network.VolleyCallback;
import lovedient.com.thelovedietandroid.network.VollyInitilization;
import lovedient.com.thelovedietandroid.utils.Constants;
import lovedient.com.thelovedietandroid.R;
import lovedient.com.thelovedietandroid.utils.EditTextHelper;
import lovedient.com.thelovedietandroid.utils.SystemPref;
import lovedient.com.thelovedietandroid.utils.SystemUtils;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG  = LoginActivity.class.getSimpleName();
    TextView login_activity_create_acount,login_activity_forgot,login_heaading
            ,password_heading,email_heading;
    Activity activity;
    CheckBox login_activity_keep_login;
    EditText login_activity_email,login_activity_password;
    CardView login_activity_btn;
    ProgressBar register_progressbar;
    private CallbackManager callbackManager;
    ImageView login_fb_btn,google_sign_btn;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeFacebook();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configureSignIn();

        activity=this;
        setXML();
    }
    public void setXML(){
        email_heading = findViewById(R.id.email_heading);
        password_heading = findViewById(R.id.password_heading);
        login_heaading = findViewById(R.id.login_heaading);
        login_activity_create_acount = findViewById(R.id.login_activity_create_acount);
        login_activity_keep_login = findViewById(R.id.login_activity_keep_login);
        login_activity_forgot = findViewById(R.id.login_activity_forgot);
        login_activity_email = findViewById(R.id.login_activity_email);
        login_activity_password = findViewById(R.id.login_activity_password);
        login_activity_btn = findViewById(R.id.login_activity_btn);
        register_progressbar = findViewById(R.id.register_progressbar);
        login_fb_btn = findViewById(R.id.login_fb_btn);
        google_sign_btn = findViewById(R.id.google_sign_btn);
        login_activity_create_acount.setOnClickListener(this);
        login_activity_btn.setOnClickListener(this);
        login_activity_forgot.setOnClickListener(this);
        login_fb_btn.setOnClickListener(this);
        google_sign_btn.setOnClickListener(this);

         // Apply Fonts
        email_heading.setTypeface(SystemUtils.applyFont());
        password_heading.setTypeface(SystemUtils.applyFont());
        login_heaading.setTypeface(SystemUtils.applyFont());
        login_activity_create_acount.setTypeface(SystemUtils.applyFont());
        login_activity_keep_login.setTypeface(SystemUtils.applyFont());
        login_activity_forgot.setTypeface(SystemUtils.applyFont());
        login_activity_forgot.setTypeface(SystemUtils.applyFont());
        login_activity_email.setTypeface(SystemUtils.applyFont());
        login_activity_password.setTypeface(SystemUtils.applyFont());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_activity_create_acount){
            SystemUtils.startActivity(activity, RegisterActivity.class, Constants.CONTINUE_MODE);
        }
        if(v.getId()==R.id.login_activity_btn){
            if(checkForm()){
                loginUser();
            }
        }
        if(v.getId()==R.id.login_activity_forgot){
            SystemUtils.startActivity(LoginActivity.this, ForgotPasswordActivity.class, Constants.CONTINUE_MODE);
        }
        if(v.getId()==R.id.login_fb_btn){
            getLoginDetails();
        }
        if(v.getId()==R.id.google_sign_btn){
            signIn();
        }
    }
    protected void initializeFacebook(){
        FacebookSdk.sdkInitialize(LoginActivity.this);
        //FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        callbackManager = CallbackManager.Factory.create();
    }
    public void loginUser(){
        register_progressbar.setVisibility(View.VISIBLE);
        final SystemPref pref = new SystemPref(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                register_progressbar.setVisibility(View.GONE);

                Log.v(TAG+" loginUser",response );
                try{
                    JSONObject dataJson = new JSONObject(response);
                    Log.v(TAG + "dataJson", dataJson.toString());
                    if(dataJson.has("status")) {
                        if (dataJson.getBoolean("status")) {
                            UserModel userModel = new UserModel();
                            JSONObject result = dataJson.getJSONObject("result");
                            if (result.has("data")) {
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
                                if(login_activity_keep_login.isChecked()){
                                    pref.saveLogin(true);
                                }
                                SystemUtils.showCustomToast(
                                        getResources().getString(R.string.login_successfully), LoginActivity.this);
                                SystemUtils.startActivity(LoginActivity.this, MainPointActivity.class , Constants.FINISH_MODE);
                            }
                        }
                    }
                }catch(Exception e){
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
                map.put(Constants.EMAIL, login_activity_email.getText().toString());
                map.put(Constants.PASSWORD, login_activity_password.getText().toString());
                return map;
            }
        };
        VollyInitilization.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
    public boolean checkForm() {
        EditTextHelper editTextHelper = new EditTextHelper();
        if (editTextHelper.isEmptyField(login_activity_password)) {
            editTextHelper.setError(login_activity_password, getResources().getString(
                    R.string.enter_passowrd
            ));
            return false;
        }
        if (editTextHelper.isEmptyField(login_activity_email)) {
            editTextHelper.setError(login_activity_email, getResources().getString(
                    R.string.enter_email
            ));
            return false;
        }
        if (!editTextHelper.isEmailValid(login_activity_email.getText().toString())) {
            editTextHelper.setError(login_activity_email, getResources().getString(
                    R.string.enter_format
            ));
            return false;
        }
        return true;
    }
    public void getLoginDetails(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Toast.makeText(getContext(),loginResult.getAccessToken().toString(),Toast.LENGTH_LONG).show();
                Profile profile = Profile.getCurrentProfile();
                Uri url =  profile.getProfilePictureUri(500,500);
                String imageShortUrl = "";
                Log.v(TAG+" ShortUrl",imageShortUrl);
                readUserProfile(AccessToken.getCurrentAccessToken().getToken().toString(),imageShortUrl);
            }
            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"cancel Request",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException error) {
                //     AccessToken.setCurrentAccessToken(null);
                Log.v("FaceBook_Error",error.toString());
                //LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivtiy.this, Arrays.asList("email"));
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    /**
     * Read User  Profile from Facebook
     * @param token
     */
    public void readUserProfile(final String token, final String url){
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String getResponse= response.toString();
                    Log.v("fbResponse",response.toString());
                    Log.v("Profile", object.toString());
                    Log.v("Profile", object.getString("email"));
                    Log.v("Profile", object.getString("first_name"));
                    Log.v("Profile", object.getString("last_name"));
                    Log.v("Profile", object.getString("id"));
                    Log.v("Profile", AccessToken.getCurrentAccessToken().getToken());
                    Log.v("Profile", url.toString());
                    UserModel userModel = new UserModel();
                    userModel.setId(object.getString("id"));
                    userModel.setUserName(object.getString("first_name")+" "+object.getString("last_name"));
                    userModel.setEmail(object.getString("email"));
                    userModel.setImagePath(url);
                    loginWithSocial(userModel);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        Bundle param = new Bundle();
        param.putString("fields","email,first_name,last_name,id");
        graphRequest.setParameters(param);
        graphRequest.executeAsync();
    }

    public void loginWithSocial(final UserModel userModel){
        register_progressbar.setVisibility(View.VISIBLE);
        final SystemPref pref = new SystemPref(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG+" Social_Login", response);
                try{
                    JSONObject dataJson = new JSONObject(response);
                    Log.v(TAG + "dataJson", dataJson.toString());
                    if(dataJson.has("status")) {
                        if (dataJson.getBoolean("status")) {
                            UserModel userModel = new UserModel();
                            JSONObject result = dataJson.getJSONObject("result");
                            if (result.has("data")) {
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
                                if(login_activity_keep_login.isChecked()){
                                    pref.saveLogin(true);
                                }
                                SystemUtils.showCustomToast(
                                        getResources().getString(R.string.login_successfully), LoginActivity.this);
                                SystemUtils.startActivity(LoginActivity.this, MainPointActivity.class , Constants.FINISH_MODE);
                            }
                        }
                    }
                }catch(Exception e){
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
                map.put(Constants.NAME, userModel.getUserName());
                map.put(Constants.EMAIL,userModel.getEmail());
                map.put(Constants.PASSWORD, "");
                map.put(Constants.GENDER, "");
                map.put(Constants.DATE_OF_BIRTH, "");
                map.put(Constants.COUNTRY, "");
                map.put(Constants.UNITS, "");
                map.put(Constants.TIME_FORMATE, "");
                map.put(Constants.USER_IMAGE,userModel.getImagePath() );
                map.put(Constants.TYPE, Constants.TYPE_FACBOOK);
                return map;
            }
        };
        VollyInitilization.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    // This method configures Google SignIn
    public void configureSignIn(){
        mAuth =  FirebaseAuth.getInstance();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]

    }

    /**
     * Sign in function
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.v(TAG+" Users",user.getEmail() );
                            Log.v(TAG+" Users_name",user.getDisplayName() );
                            Log.v(TAG+" Users_image",user.getPhotoUrl().toString() );
                            UserModel userModel = new UserModel();
                            userModel.setEmail(user.getEmail());
                            userModel.setUserName(user.getDisplayName());
                            userModel.setImagePath(user.getPhotoUrl().toString() );
                            loginWithSocial(userModel);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        //    updateUI(null);
                        }

                        // ...
                    }
                });
    }
}

