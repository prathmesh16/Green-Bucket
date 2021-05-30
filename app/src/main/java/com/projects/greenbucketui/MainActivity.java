package com.projects.greenbucketui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.projects.greenbucketui.API.Api_Constants;
import com.projects.greenbucketui.Adapter.ProductListAdapter;
import com.projects.greenbucketui.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ProductListAdapter.setCartDetails {

    ViewFlipper img_carousel;
    EditText searchEt;
    private RecyclerView rvProduct;
    private Product product;
    private ProductListAdapter productListAdapter;
    private ArrayList<Product> list;
    private Button gotoCart;
    ImageView drawerBtn;
    NavigationView navigationView;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle ;
    int LAUNCH_SECOND_ACTIVITY = 1;
    ImageView cart ;
    TextView price,quantity,header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Images to be displayed in the slider
        int images[] = {R.drawable.banner1,R.drawable.delivery2door2,R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};
        list = new ArrayList<>();
        cart=findViewById(R.id.carticon);
        price=findViewById(R.id.total_price);
        quantity=findViewById(R.id.cart_quantity);
        img_carousel = findViewById(R.id.home_img_carousel);
        rvProduct = findViewById(R.id.rvProductList);
        searchEt = findViewById(R.id.searchEditText);
        header = findViewById(R.id.homeHeader);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                //startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
                startActivity(intent);
            }
        });

        DBHelper db=new DBHelper(this);
        ArrayList<CartItem> cartData;
        cartData=db.getAllData();
        int total_price=0,total_quantity=0;
        total_quantity=cartData.size();
        for(CartItem c:cartData)
        {
            total_price+=c.getPrice()*c.getQuantity();
        }
        price.setText(total_price+"");
        quantity.setText(total_quantity+"");
        gotoCart = findViewById(R.id.btnGotoCart);

        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);

                //startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
                startActivity(intent);
            }
        });

        //getProductData();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+"91 9422249498";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        for (int image: images){
            setImg(image);
        }
        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green));

        navigationView=findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerBtn=findViewById(R.id.menu_btn);
        mDrawerlayout = findViewById(R.id.drawer);

        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerlayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerlayout.closeDrawer(GravityCompat.START);
                } else
                    mDrawerlayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void getProductData()
    {
        rvProduct.setHasFixedSize(true);
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        list.clear();
        JSONObject request = new JSONObject();
        try {

            request.put("MethodName","product");
            request.put("Action","All");

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
                        DBHelper db=new DBHelper(MainActivity.this);
                        //TODO : fetch api data
                        try {
                            JSONArray data = response.getJSONArray("data");
                            //Log.e("data",data.toString());
                            //JSONObject  WHA1=data.getJSONObject(0);
                            for(int i=0;i<data.length();i++)
                            {

                                JSONObject obj =data.getJSONObject(i);

                                product = new Product(db.getQuantity(obj.getInt("id")),obj.getString("CategoryName"),""+obj.getInt("id"),obj.getString("category_id"),obj.getString("product_name"),obj.getString("product_detail"),obj.getString("product_price"),obj.getString("product_image_short"),obj.getString("Quantity"));
                               // Log.e("abcd",obj.getString("product_name")+" "+product.getProduct_name());
                                list.add(product);
                                Log.e("added",list.get(i).getProduct_name());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        productListAdapter = new ProductListAdapter(MainActivity.this,list,MainActivity.this);
                        rvProduct.setAdapter(productListAdapter);


                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", anError.getMessage());
                    }
                });

    }

    public void setImg (int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        img_carousel.addView(imageView);
        img_carousel.setFlipInterval(3000); //Images will change after 3 seconds
        img_carousel.setAutoStart(true);

        // Image animation
        img_carousel.setInAnimation(this,android.R.anim.slide_in_left);
        img_carousel.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    private void filter(String text) {

        ArrayList<Product> filteredList = new ArrayList<>();

        for(Product dataMaster : list)
        {
            int i =0;
            Log.e("list data",list.get(i).getCategoryName());
            if(dataMaster.getProduct_name().toLowerCase().contains(text.toLowerCase()))
            {

                filteredList.add(dataMaster);
            }
            i++;
        }
        productListAdapter.filterList(filteredList);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return false;
    }
    @Override
    public void onResume()
    {
        getProductData();
        setDetails();
        super.onResume();

    }

    @Override
    public void setDetails() {

        DBHelper db=new DBHelper(this);
        ArrayList<CartItem> cartData;
        cartData=db.getAllData();
        int total_price=0,total_quantity=0;
        total_quantity=cartData.size();
        for(CartItem c:cartData)
        {
            total_price+=c.getPrice()*c.getQuantity();
        }
        price.setText(total_price+"");
        quantity.setText(total_quantity+"");
    }
}