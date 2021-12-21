package com.example.techapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techapp.Activity_Prod_Info;
import com.example.techapp.R;
import com.example.techapp.modeles.Produit;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<Produit,RecyclerViewAdapter.ViewHolder> {
    static ArrayList<Produit> favoriteslist=new ArrayList();
    ImageButton addfavorite;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference,reference1;
    View view;




    private boolean rechercher(ArrayList<Produit> produits,Produit p){
        boolean result=false;
        for(Produit c:produits){
            if(c.getName().equals(p.getName()) && c.getBrand().equals(p.getBrand()) && c.getImageurl().equals(p.getImageurl()))
            {
                result=true;
                break;
            }
        }
        return result;
    }






    public RecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<Produit> options){
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_row,parent ,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Produit model) {
        

        holder.name.setText(model.getName());
        holder.brand.setText(model.getBrand());
        holder.price.setText(new StringBuilder().append("$ ").append(model.getPrice()).toString());
        reference1=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites");

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(view.getContext(), Activity_Prod_Info.class);
                i.putExtra("name",model.getName());
                i.putExtra("brand",model.getBrand());
                i.putExtra("price",model.getPrice());
                i.putExtra("image",model.getImageurl());
                i.putExtra("description",model.getDescription());
                i.putExtra("type",model.getType());
                view.getContext().startActivity(i);
            }


        });

        holder.imagebutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                favoriteslist.add(model);
                reference=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites").child(model.getName()+" "+model.getBrand());
                    reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            holder.imagebutton.setImageResource(R.drawable.ic_heart_checked);
                        }
                    });






            }
        });
        Picasso.get().load(model.getImageurl()).into(holder.imageView);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    favoriteslist.clear();

                    for(DataSnapshot data:snapshot.getChildren()){
                        Produit p=data.getValue(Produit
                                .class);
                        favoriteslist.add(p);
                    }

                    if(rechercher(favoriteslist,model)){
                        holder.imagebutton.setImageResource(R.drawable.ic_heart_checked);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }




    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name,brand,price;
        ImageView imageView;
        ImageButton imagebutton;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            brand=itemView.findViewById(R.id.brand);
            price=itemView.findViewById(R.id.price);
            imageView=itemView.findViewById(R.id.imageView);
            imagebutton=itemView.findViewById(R.id.addfavorite);
            cardview=itemView.findViewById(R.id.cardview);


        }



    }

}
