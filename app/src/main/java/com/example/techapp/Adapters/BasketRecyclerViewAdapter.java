package com.example.techapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techapp.R;
import com.example.techapp.modeles.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BasketRecyclerViewAdapter extends FirebaseRecyclerAdapter<Produit,BasketRecyclerViewAdapter.ViewHolder>{
    Context context;
    ArrayList<Produit> produits;
    View view;

    DatabaseReference reference,reference1;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();



    public BasketRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Produit> options){
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_recycleview_row,parent ,false);


        return new BasketRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRecyclerViewAdapter.ViewHolder holder, int position, @NonNull Produit model) {

        holder.name.setText(model.getName());
        holder.brand.setText(model.getBrand());
        holder.price.setText(new StringBuilder().append("$ ").append(model.getPrice()).toString());
        holder.quantite.setText(new StringBuilder().append(model.getQuantite()).toString());
        Picasso.get().load(model.getImageurl()).into(holder.imageView);



        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("name",model.getName());
                hashMap.put("imageurl",model.getImageurl());
                hashMap.put("brand",model.getBrand());
                hashMap.put("price",model.getPrice());
                hashMap.put("description",model.getDescription());
                hashMap.put("quantite",model.getQuantite()+1);
                reference=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("basket");
                reference.child(model.getName()+" "+model.getBrand()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        model.setQuantite(model.getQuantite()+1);
                        holder.quantite.setText(new StringBuilder().append(model.getQuantite()).toString());
                    }
                });
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("name",model.getName());
                hashMap.put("imageurl",model.getImageurl());
                hashMap.put("brand",model.getBrand());
                hashMap.put("price",model.getPrice());
                hashMap.put("description",model.getDescription());
                hashMap.put("quantite",model.getQuantite()-1);
                reference=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("basket");
                reference.child(model.getName()+" "+model.getBrand()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        model.setQuantite(model.getQuantite()+1);
                        holder.quantite.setText(new StringBuilder().append(model.getQuantite()).toString());
                    }
                });
            }
        });

        holder.deletefrombasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference1=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("basket").child(model.getName()+" "+model.getBrand());
                reference1.setValue(null);
            }
        });
    }




    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name,brand,price,quantite;
        ImageView imageView;
        Button plus,minus;
        ImageButton deletefrombasket;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantite=itemView.findViewById(R.id.quantité);
            name=itemView.findViewById(R.id.name2);
            brand=itemView.findViewById(R.id.brand2);
            price=itemView.findViewById(R.id.price2);
            deletefrombasket=itemView.findViewById(R.id.deletefrombasket);
            imageView=itemView.findViewById(R.id.imageView2);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            quantite=itemView.findViewById(R.id.quantité);
        }

    }
}
