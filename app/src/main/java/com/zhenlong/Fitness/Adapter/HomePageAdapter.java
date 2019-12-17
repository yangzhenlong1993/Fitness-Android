package com.zhenlong.Fitness.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenlong.Fitness.R;

import java.util.ArrayList;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyViewHolder> {
private ArrayList<Bitmap> imgs;

public HomePageAdapter(ArrayList<Bitmap> imgs){
    this.imgs = imgs;
}

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    Bitmap bitmap = imgs.get(position % imgs.size());
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.homepage_item_view);
        }
    }
}
