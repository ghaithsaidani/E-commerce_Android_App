package com.example.techapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techapp.R;
import com.example.techapp.modeles.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritesRecyclerViewAdapter extends FirebaseRecyclerAdapter<Produit,FavoritesRecyclerViewAdapter.ViewHolder> {




    public FavoritesRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Produit> options){
        super(options);
    }

    @NonNull
    @Override
    public FavoritesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_recycleview_row,parent ,false);
        return new FavoritesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Produit model) {

        holder.name.setText(model.getName());
        holder.brand.setText(model.getBrand());
        holder.price.setText(new StringBuilder().append("$ ").append(model.getPrice()).toString());

        Picasso.get().load(model.getImageurl()).into(holder.imageView);
    }



    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name,brand,price;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name1);
            brand=itemView.findViewById(R.id.brand1);
            price=itemView.findViewById(R.id.price1);
            imageView=itemView.findViewById(R.id.imageView1);
        }

    }
}
