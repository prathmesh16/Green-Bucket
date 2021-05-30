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

import com.projects.greenbucketui.Adapter.Adapter_Cart;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements Adapter_Cart.setPriceInterface{
    DBHelper mydb;
    ArrayList<CartItem> data;
    RecyclerView cartItemView;
    Adapter_Cart adapter_cart;
    private SharedPreferences adr_save;


    Button checkout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mydb = new DBHelper(this);

        cartItemView=findViewById(R.id.cart_list_rv);
        adr_save=getSharedPreferences("profile",MODE_PRIVATE);

        //for inserting data in sqlite
//        mydb.insertData(300,"Apple","abcd",99,2,"Fruit");
//        mydb.insertData(400,"Banana","abcd",121,2,"Fruit");

        //for retruveing all items from sqlite into list of cartiem object;
        setPrice();
        data=mydb.getAllData();
        int totalPrice=0;
        for(CartItem c:data)
        {
            totalPrice+=c.getPrice()*c.getQuantity();
        }
        TextView total=findViewById(R.id.total_amt);
        total.setText(totalPrice+"");
        //update data in sqlite
        //mydb.updateData(100,"banana","abcd",123,2,"Fruit");

        //delete data from sqlite
        //mydb.deleteData(100);
        cartItemView.setHasFixedSize(true);
        cartItemView.setLayoutManager(new LinearLayoutManager(this));
        adapter_cart=new Adapter_Cart(data,this,this);
        cartItemView.setAdapter(adapter_cart);

        checkout_btn = findViewById(R.id.checkout_btn);

        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adr_save.getString("status","").equalsIgnoreCase("true"))
                {
                    Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(CartActivity.this, "Please Login First.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


            }
        });

        // Change the colour of action bar
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.cart_page) + "</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));

        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green));
    //dataFetch();
    }



    @Override
    public void setPrice(){
        int totalPrice=0;
        data=mydb.getAllData();
        for(CartItem c:data)
        {
            totalPrice+=c.getPrice()*c.getQuantity();
        }
        TextView total=findViewById(R.id.total_amt);
        total.setText(totalPrice+"");
    }
    void dataFetch()
    {
        ArrayList<CartItem> data = mydb.getAllData();
        for(CartItem a:data)
        {
            Log.e("qua",a.getQuantity()+"");
            Log.e("nom",a.getName()+"");

        }
    }
}