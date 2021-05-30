package com.projects.greenbucketui.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.projects.greenbucketui.CartItem;
import com.projects.greenbucketui.DBHelper;
import com.projects.greenbucketui.R;

import java.util.ArrayList;

public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.MyViewHolder> {

    ArrayList<CartItem> dataCartItems;
    Context context;
    DBHelper dbHelper;
    setPriceInterface setPriceInterface;
    public Adapter_Cart(ArrayList<CartItem> dataCartItems, Context context,setPriceInterface setPriceInterface) {
        this.dataCartItems = dataCartItems;
        this.context = context;
        dbHelper=new DBHelper(context);
        this.setPriceInterface=setPriceInterface;
    }

    @NonNull
    @Override
    public Adapter_Cart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_list_rv_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_Cart.MyViewHolder holder, final int position) {

        holder.name.setText(dataCartItems.get(position).getName());
        holder.catagory.setText(dataCartItems.get(position).getCatagory());
        holder.quantity.setText(""+dataCartItems.get(position).getQuantity());
        holder.price.setText(""+dataCartItems.get(position).getPrice());

        final String url = dataCartItems.get(position).getImage();

        Log.e("url",url);

        Glide.with(context)
                .load(url).placeholder(R.drawable.apples).into(holder.image);

        //set image url by picasso or glide
        //set quanity to spinner
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteData(dataCartItems.get(position).getId());
                dataCartItems.remove(position);
                setPriceInterface.setPrice();
                notifyDataSetChanged();
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = dataCartItems.get(position).getQuantity();
                dbHelper.updateData(dataCartItems.get(position).getId(),dataCartItems.get(position).getName(),url,(int)Float.parseFloat(dataCartItems.get(position).getPrice()+""),x+1,dataCartItems.get(position).getCatagory());

                dataCartItems.get(position).setQuantity(x+1);
                holder.quantity.setText(String.valueOf(x+1));
                setPriceInterface.setPrice();
            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = dataCartItems.get(position).getQuantity();
                if(dataCartItems.get(position).getQuantity()==1)
                {
                    dataCartItems.get(position).setQuantity(x-1);
                   // holder.add.setVisibility(View.VISIBLE);
                   // holder.item_quantity.setVisibility(View.GONE);
                    dbHelper.deleteData(dataCartItems.get(position).getId());
                    dataCartItems.remove(position);
                    setPriceInterface.setPrice();
                    notifyDataSetChanged();
                }
                else
                {

                    dbHelper.updateData(dataCartItems.get(position).getId(),dataCartItems.get(position).getName(),url,(int)Float.parseFloat(dataCartItems.get(position).getPrice()+""),x-1,dataCartItems.get(position).getCatagory());
                    dataCartItems.get(position).setQuantity(x-1);
                    holder.quantity.setText(String.valueOf(x-1));
                    setPriceInterface.setPrice();
                }

            }
        });
       
    }

    @Override
    public int getItemCount() {
        return dataCartItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView price,name,catagory;
        ImageView image;
        TextView quantity;
        Button remove,sub,add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cart_list_name);
            catagory = itemView.findViewById(R.id.cart_list_category);
            //sender = itemView.findViewById(R.id.sender);

            price = itemView.findViewById(R.id.cart_list_price);

            image = itemView.findViewById(R.id.cart_list_img);
            quantity=itemView.findViewById(R.id.item_quantity_cart_tv);
            remove=itemView.findViewById(R.id.remove);

            sub=itemView.findViewById(R.id.subtract_item_cart_btn);
            add=itemView.findViewById(R.id.add_item_cart_btn);

        }
    }
    public interface setPriceInterface
    {
        void setPrice();
    }
}
