package com.projects.greenbucketui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;

import com.projects.greenbucketui.Adapter.Adapter_Adress;
import com.projects.greenbucketui.Adapter.ProductListAdapter;

public class ProductDetailsActivity extends AppCompatActivity {

    //private Adapter_Adress adapter_adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_activty);

        // Change the colour of action bar
        getSupportActionBar().setElevation(0);
        //getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.fruits) + "</font>")); //use vegetable for vegetables page
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));           //Colour of action bar

        //Change the colour of status bar/ notification bar
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green));

        //adapter_adress = new Adapter_Adress();


    }

}