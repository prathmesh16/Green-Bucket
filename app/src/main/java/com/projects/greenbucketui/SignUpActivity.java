package com.projects.greenbucketui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.projects.greenbucketui.API.Api_Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    Button user_register;
    EditText name,society,address,city,state,email,mob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acitvity);

        user_register = findViewById(R.id.user_register);
        name = findViewById(R.id.user_name);
        society = findViewById(R.id.society_name);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city_name);
        state = findViewById(R.id.state_name);
        email = findViewById(R.id.user_email);
        mob = findViewById(R.id.user_phone);

        user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                startActivity(intent);

                if(validate())
                {
                    register();
                }
            }
        });

        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.background));
    }

    private boolean validate() {

        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("This field can not be blank");
            return false;
        }
        else if (society.getText().toString().trim().equalsIgnoreCase("")) {
            society.setError("This field can not be blank");
            return false;

        }
        else if (address.getText().toString().trim().equalsIgnoreCase("")) {
            address.setError("This field can not be blank");
            return false;

        }
        else if (city.getText().toString().trim().equalsIgnoreCase("")) {
            city.setError("This field can not be blank");
            return false;

        }
        else if (state.getText().toString().trim().equalsIgnoreCase("")) {
            state.setError("This field can not be blank");
            return false;

        }
        else if (email.getText().toString().trim().equalsIgnoreCase("")) {
            email.setError("This field can not be blank");
            return false;

        }
        else if (mob.getText().toString().trim().equalsIgnoreCase("")) {
            mob.setError("This field can not be blank");
            return false;
        }
        else if (mob.getText().length()!=10) {
            mob.setError("Invalid Mobile Number");
            return false;
        }
        else
        {
            return true;
        }

    }

    private void register() {

        JSONObject request = new JSONObject();
        try {

            request.put("MethodName","register");
            request.put("name",name.getText().toString());
            request.put("adresss",address.getText().toString());
            request.put("society",society.getText().toString());
            request.put("city",city.getText().toString());
            request.put("state",state.getText().toString());
            request.put("mobile",mob.getText().toString());
            request.put("email",email.getText().toString());


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

                        Log.e("res",response.toString());

                        //TODO : fetch api data
                        try {
                            JSONArray data = response.getJSONArray("data");
                            //Log.e("data",data.toString());
                            //JSONObject  WHA1=data.getJSONObject(0);

                            JSONObject msg = data.getJSONObject(0);
                            //Log.e("data msg",msg.getString("Column1"));

                            if(msg.getString("Column1").equalsIgnoreCase("Record already Register"))
                            {

                                Toast.makeText(SignUpActivity.this,"User Already Registered",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this,"User Registered Succefully.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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
}