package com.example.techapp.ui.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techapp.Adapters.FavoritesRecyclerViewAdapter;
import com.example.techapp.R;
import com.example.techapp.linearlayout.WrapContentLinearLayoutManager;
import com.example.techapp.modeles.Produit;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel mViewModel;
    ArrayList<Produit> produitslist;
    FavoritesRecyclerViewAdapter adapter;
    View myfragment;
    RecyclerView favoritesrecyclerview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myfragment= inflater.inflate(R.layout.fragment_favorites, container, false);

        favoritesrecyclerview=myfragment.findViewById(R.id.favorites_recyclerview);
        favoritesrecyclerview.setHasFixedSize(true);
        firebaseStorage=FirebaseStorage.getInstance();
        favoritesrecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));


        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites");

        FirebaseRecyclerOptions<Produit> options=new FirebaseRecyclerOptions.Builder<Produit>().setQuery(databaseReference,Produit.class).build();
        adapter=new FavoritesRecyclerViewAdapter(options);
        favoritesrecyclerview.setAdapter(adapter);
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



}