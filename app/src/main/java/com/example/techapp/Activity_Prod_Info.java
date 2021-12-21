package com.example.techapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.techapp.modeles.Produit;
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

public class Activity_Prod_Info extends AppCompatActivity {
    TextView name,brand,price,description;
    ImageView ImageUrl;
    Button addtobasket;
    ImageButton delete,edit;
    static ArrayList<Produit> basket=new ArrayList<>();
    DatabaseReference reference,reference1;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onStart() {
        super.onStart();

        name =findViewById(R.id.Nom);
        price =findViewById(R.id.Price);
        ImageUrl =findViewById(R.id.ImageUrl);
        description=findViewById(R.id.description);
        addtobasket=findViewById(R.id.addtobasket);
        delete=findViewById(R.id.delete);
        edit=findViewById(R.id.editimagebutton);
        String name1=getIntent().getStringExtra("name");
        String brand1=getIntent().getStringExtra("brand");
        String description1=getIntent().getStringExtra("description");
        String type=getIntent().getStringExtra("type");
        int price1=getIntent().getIntExtra("price",0);
        name.setText(name1+" "+brand1);


        price.setText("$ "+String.valueOf(price1));
        description.setText(description1);
        /*Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            int image1= bundle.getInt("image");}*/


        String image1=getIntent().getExtras().getString("image");
        Picasso.get().load(image1).into(ImageUrl);

        Produit p=new Produit(image1,name1,brand1,description1,type,price1,1);

        switch(type){
            case "Wearable":
                reference1=firebaseDatabase.getInstance().getReference("Products").child("Wearable").child(name1+" "+brand1);
                break;
            case "Laptop":
                reference1=firebaseDatabase.getInstance().getReference("Products").child("Laptops").child(name1+" "+brand1);
                break;
            case "Drone":
                reference1=firebaseDatabase.getInstance().getReference("Products").child("Drones").child(name1+" "+brand1);
                break;
            case "Phone":
                reference1=firebaseDatabase.getInstance().getReference("Products").child("Phones").child(name1+" "+brand1);
                break;
        }





        reference=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("basket").child(name1+" "+brand1);


        addtobasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                basket.add(p);

                reference.setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Activity_Prod_Info.this,"Product added to basket",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference1.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i=new Intent(Activity_Prod_Info.this,MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(Activity_Prod_Info.this,Activity_Update_Produit.class);
                startActivity(i);
                i.putExtra("name",name1);
                i.putExtra("brand",brand1);
                i.putExtra("price",price1);
                i.putExtra("image",image1);
                i.putExtra("description",description1);
                i.putExtra("type",type);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.fragment_prod_info);


        }







    }

