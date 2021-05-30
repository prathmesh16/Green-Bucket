package com.projects.greenbucketui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.greenbucketui.DBHelper;
import com.projects.greenbucketui.Model.Product;
import com.projects.greenbucketui.R;

import java.util.ArrayList;
import java.util.List;



public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.viewHolder> {


    private Context mCtx;
    private ArrayList<Product> productList;
    setCartDetails setCartDetails;
    public ProductListAdapter(Context mCtx, ArrayList<Product> productList,setCartDetails setCartDetails) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.setCartDetails=setCartDetails;
    }

    @NonNull
    @Override
    public ProductListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(mCtx).inflate(R.layout.product_list_rv_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final ProductListAdapter.viewHolder holder, final int position) {

        final String name = productList.get(position).getProduct_name();
        final String cat = productList.get(position).getCategoryName();
        final String mrp = productList.get(position).getProduct_price();
        final String dis_price = productList.get(position).getProduct_price();
        final String url = productList.get(position).getProduct_image_short();


        //setting image and name,description,price
        //holder.setProductImage(productImageLink);
        holder.name.setText(productList.get(position).getProduct_name());
        holder.cat.setText(cat);
        holder.mrp.setText(mrp);
        holder.dis_price.setText(dis_price);

        if(productList.get(position).getSelectedQuantity()>0)
        {
            holder.add.setVisibility(View.GONE);
            holder.item_quantity.setVisibility(View.VISIBLE);
            holder.quantity.setText(String.valueOf(productList.get(position).getSelectedQuantity()));
        }
        final DBHelper mydb=new DBHelper(mCtx);
// with placeholder
        Glide.with(mCtx)
                .load(url).placeholder(R.drawable.apples).into(holder.img);

        holder.add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                holder.add.setVisibility(View.GONE);
                holder.item_quantity.setVisibility(View.VISIBLE);


                int x = productList.get(position).getSelectedQuantity();
                productList.get(position).setSelectedQuantity(x+1);
                holder.quantity.setText(String.valueOf(x+1));

                mydb.insertData(Integer.parseInt(productList.get(position).getId()),name,url,(int)Float.parseFloat(mrp),1,cat);
                setCartDetails.setDetails();
            }
        });

        holder.minusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               // Log.e("its clicl","ues");

                int x = productList.get(position).getSelectedQuantity();


                if(productList.get(position).getSelectedQuantity()==1)
                {
                    productList.get(position).setSelectedQuantity(x-1);
                    holder.add.setVisibility(View.VISIBLE);
                    holder.item_quantity.setVisibility(View.GONE);
                    mydb.deleteData(Integer.parseInt(productList.get(position).getId()));

                }
                else
                {
                    mydb.updateData(Integer.parseInt(productList.get(position).getId()),name,url,(int)Float.parseFloat(mrp),x-1,cat);
                    productList.get(position).setSelectedQuantity(x-1);
                    holder.quantity.setText(String.valueOf(x-1));
                }
                setCartDetails.setDetails();
            }
        });

        holder.plusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.e("its clicl","ues");

                int x = productList.get(position).getSelectedQuantity();
                if(x==0)
                {
                    productList.get(position).setSelectedQuantity(x+1);
                }
                mydb.updateData(Integer.parseInt(productList.get(position).getId()),name,url,(int)Float.parseFloat(mrp),x+1,cat);
                productList.get(position).setSelectedQuantity(x+1);
                holder.quantity.setText(String.valueOf(x+1));
                setCartDetails.setDetails();
            }
        });
    }
    public void filterList(ArrayList<Product> filteredList)
    {
        productList = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
    public interface setCartDetails
    {
        void setDetails();
    }
    public class viewHolder extends RecyclerView.ViewHolder {

        TextView name,cat,mrp,dis_price,quantity;
        Button add,plusBtn,minusBtn;
        ImageView img;
        LinearLayout item_quantity;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_list_name);
            cat = itemView.findViewById(R.id.product_list_category);
            mrp = itemView.findViewById(R.id.product_list_mrp);
            dis_price = itemView.findViewById(R.id.product_list_discounted_price);
            add= itemView.findViewById(R.id.add);
            img = itemView.findViewById(R.id.product_list_img);

            add = itemView.findViewById(R.id.add);
            item_quantity = itemView.findViewById(R.id.item_quantity_ll);

            plusBtn = itemView.findViewById(R.id.add_item_btn);
            minusBtn = itemView.findViewById(R.id.subtract_item_btn);
            quantity = itemView.findViewById(R.id.item_quantity_tv);





        }
    }
}
