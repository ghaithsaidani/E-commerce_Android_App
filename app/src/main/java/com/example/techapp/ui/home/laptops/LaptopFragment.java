package com.example.techapp.ui.home.laptops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techapp.Adapters.RecyclerViewAdapter;
import com.example.techapp.R;
import com.example.techapp.linearlayout.WrapContentLinearLayoutManager;
import com.example.techapp.modeles.Produit;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class LaptopFragment extends Fragment {


    ArrayList<Produit> produitslist;
    RecyclerViewAdapter adapter;
    View myfragment;
    RecyclerView laptoprecyclerview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        myfragment= inflater.inflate(R.layout.fragment_laptop, container, false);
        laptoprecyclerview=myfragment.findViewById(R.id.laptop_recyclerview);
        laptoprecyclerview.setHasFixedSize(true);
        firebaseStorage=FirebaseStorage.getInstance();
        laptoprecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false));

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Products/Laptops");
        FirebaseRecyclerOptions<Produit> options=new FirebaseRecyclerOptions.Builder<Produit>().setQuery(databaseReference,Produit.class).build();
        adapter=new RecyclerViewAdapter(options);

        laptoprecyclerview.setAdapter(adapter);







        return myfragment;

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);




    }

}