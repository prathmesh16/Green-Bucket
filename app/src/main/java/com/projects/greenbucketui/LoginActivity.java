package com.projects.greenbucketui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.projects.greenbucketui.API.Api_Constants;
import com.projects.greenbucketui.Adapter.ProductListAdapter;
import com.projects.greenbucketui.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText mobile, otp_et;
    TextView skip;
    Button otp, login;
    TextView redirect_to_signup;
    private SharedPreferences adr_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobile = findViewById(R.id.mobile);
        otp_et = findViewById(R.id.otp_et);
        skip = findViewById(R.id.tvSkip);

        otp = findViewById(R.id.otp);
        login = findViewById(R.id.login);

        adr_save=getSharedPreferences("profile",MODE_PRIVATE);

        redirect_to_signup = findViewById(R.id.redirect_to_signup);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mobile.length()!=10)
                {
                    mobile.setError("Invalid Mobile Number");
                }
                else
                {
                    sendOtp();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setClickable(false);
                verfiyOtp();
            }
        });

        redirect_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.background));
    }

    private void verfiyOtp() {

        JSONObject request = new JSONObject();
        try {

            request.put("MethodName","login");
            request.put("action","select");
            request.put("mobile",mobile.getText().toString());
            request.put("otp",otp_et.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(Api_Constants.mainApi)
                .addUrlEncodeFormBodyParameter("Request",request.toString())
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Log.e("res",response.toString());

                        //TODO : fetch api data
                        try {
                            //JSONArray data = response.getJSONArray("data");
                            //Log.e("data",data.toString());
                            //JSONObject  WHA1=data.getJSONObject(0);
                            //Log.e("data>>>>>",response.getString("statuscode"));

                            //login.setClickable(true);

                            if(response.getString("statuscode").equalsIgnoreCase("err"))
                            {
                                Toast.makeText(LoginActivity.this,"Wrong Otp",Toast.LENGTH_LONG).show();
                            }
                            else
                            {


                                try {
                                    JSONArray data = response.getJSONArray("data");
                                    //Log.e("data",data.toString());
                                    //JSONObject  WHA1=data.getJSONObject(0);

                                    JSONObject obj = data.getJSONObject(0);
                                    adr_save.edit().putString("name", obj.getString("TBL_ClientName")).commit();
                                    adr_save.edit().putString("uid", obj.getString("TBL_C_ID")).commit();
                                    adr_save.edit().putString("adr", obj.getString("TBL_ClientAdd")).commit();
                                    adr_save.edit().putString("society", obj.getString("TBL_Society")).commit();
                                    adr_save.edit().putString("city", obj.getString("TBL_City")).commit();
                                    adr_save.edit().putString("mob", obj.getString("TBL_MobileNo")).commit();
                                    adr_save.edit().putString("email", obj.getString("TBL_Email_ID")).commit();
                                    adr_save.edit().putString("status", "true").commit();


                                    String s =adr_save.getString("name","");
                                    Log.e("name from server",s);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", anError.getMessage());
                    }
                });

    }

    private void sendOtp() {

        JSONObject request = new JSONObject();
        try {

            request.put("MethodName","login");
            request.put("action","insert");
            request.put("mobile",mobile.getText().toString());
            request.put("otp","123");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(Api_Constants.mainApi)
                .addUrlEncodeFormBodyParameter("Request",request.toString())
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //get data in item object
                        //create item object by data
                        //add one by one objects to list
                        //create adapter and add list
                        //set adapter to recyclerview
                        Log.e("res",response.toString());

                        //TODO : fetch api data
                        try {
                            JSONArray data = response.getJSONArray("data");
                            //Log.e("data",data.toString());
                            //JSONObject  WHA1=data.getJSONObject(0);

                            JSONObject msg = data.getJSONObject(0);
                            //Log.e("data msg",msg.getString("Column1"));

                            if(msg.getString("Column1").equalsIgnoreCase("OTP Send Successfully"))
                            {

                                Toast.makeText(LoginActivity.this,"OTP Sent",Toast.LENGTH_LONG).show();

                                mobile.setVisibility(View.GONE);   // Hide Mobile Number ET
                                otp_et.setVisibility(View.VISIBLE);  // Show OTP ET
                                skip.setVisibility(View.GONE);

                                otp.setVisibility(View.GONE);  // Hide Generate OTP Button
                                login.setVisibility(View.VISIBLE);  // Show Login Button
                                redirect_to_signup.setVisibility(View.GONE); // Hide SignUp option
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Number Not registred",Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", anError.getMessage());
                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
        
    }
}