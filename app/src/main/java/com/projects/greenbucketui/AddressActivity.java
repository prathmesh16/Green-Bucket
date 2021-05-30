package com.projects.greenbucketui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.projects.greenbucketui.API.Api_Constants;
import com.projects.greenbucketui.Adapter.Adapter_Adress;
import com.projects.greenbucketui.PaymentModule.PaymentScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    Adapter_Adress adapter_adress;
    RecyclerView rvAdress;
    Button payment;
    private SharedPreferences adr_save;
    DBHelper mydb;
    ArrayList<CartItem> data;
    TextView totalamt;




    int totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // Change the colour of action bar
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.address_page) + "</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));

        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green));

        payment = findViewById(R.id.go_to_payment_btn);
        mydb = new DBHelper(this);
        data=mydb.getAllData();
        totalamt = findViewById(R.id.total_amt_add);


        rvAdress = findViewById(R.id.address_list_rv);
        rvAdress.setHasFixedSize(true);
        rvAdress.setLayoutManager(new LinearLayoutManager(this));

        adr_save=getSharedPreferences("profile",MODE_PRIVATE);


        String adr = adr_save.getString("adr","");
        String sco = adr_save.getString("society","");
        String city = adr_save.getString("city","");



        //Log.e("adress save",s);
        adapter_adress = new Adapter_Adress(this,adr+","+sco+","+city);

        rvAdress.setAdapter(adapter_adress);
        totalPrice=0;
        for(CartItem c:data)
        {
            totalPrice+=c.getPrice()*c.getQuantity();
        }
        totalamt.setText(totalPrice+"");


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRequest();
            }
        });

    }


    private void orderRequest() {

        String s =adr_save.getString("name","");
        Log.e("name from server",s);

        JSONObject request = new JSONObject();
        try {

            request.put("MethodName", "order");
            request.put("memberid", adr_save.getString("uid", ""));
            request.put("txn_amount", "123");
            request.put("c_mobile", adr_save.getString("mob", ""));
            request.put("c_address", adr_save.getString("adr", ""));
            request.put("c_society", adr_save.getString("society", ""));
            request.put("c_b_address", adr_save.getString("adr", ""));
            request.put("c_email", adr_save.getString("email", ""));
            request.put("p_mode", "cash");

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
                            JSONObject  WHA1=data.getJSONObject(0);
                            Toast.makeText(AddressActivity.this,"Order id :"+WHA1.getString("ORDERID"),Toast.LENGTH_LONG).show();
                            //submitRequest(WHA1.getString("ORDERID"));

                            Intent intent = new Intent(AddressActivity.this, PaymentScreen.class);
                            intent.putExtra("ORDERID",WHA1.getString("ORDERID"));
                            intent.putExtra("Amount",totalPrice);
                            startActivity(intent);
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