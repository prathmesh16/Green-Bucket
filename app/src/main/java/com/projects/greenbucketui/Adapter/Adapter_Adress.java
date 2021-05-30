package com.projects.greenbucketui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.greenbucketui.R;

public class Adapter_Adress extends RecyclerView.Adapter<Adapter_Adress.MyViewHolder>
    {

        Context context;
        String adress;

        public Adapter_Adress(Context context, String adress) {
            this.context = context;
            this.adress = adress;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.address_list_rv_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.address.setText(adress);

        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView address;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                address = itemView.findViewById(R.id.full_address);


            }
        }
    }