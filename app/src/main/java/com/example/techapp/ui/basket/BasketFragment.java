package com.example.techapp.ui.basket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.Adapters.BasketRecyclerViewAdapter;
import com.example.techapp.R;
import com.example.techapp.linearlayout.WrapContentLinearLayoutManager;
import com.example.techapp.modeles.Produit;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class BasketFragment extends Fragment {

    private BasketViewModel mViewModel;

    List<Produit>  produitslist;
    BasketRecyclerViewAdapter adapter;
    View myfragment;
    Button checkout;
    RecyclerView basketrecyclerview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;

    TextView totalprice;
    int basketprice=0;

    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myfragment= inflater.inflate(R.layout.fragment_basket, container, false);
        totalprice=myfragment.findViewById(R.id.totalprice);
        checkout=myfragment.findViewById(R.id.checkout);
        produitslist=new ArrayList<>();
        basketrecyclerview=myfragment.findViewById(R.id.basket_recyclerview);
        basketrecyclerview.setHasFixedSize(true);

        basketrecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("basket");


        FirebaseRecyclerOptions<Produit> options=new FirebaseRecyclerOptions.Builder<Produit>().setQuery(databaseReference,Produit.class).build();
        adapter=new BasketRecyclerViewAdapter(options);
        basketrecyclerview.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    produitslist.clear();
                    basketprice=0;
                    for(DataSnapshot data:snapshot.getChildren()){
                        Produit p=data.getValue(Produit
                                .class);
                        produitslist.add(p);
                    }

                    for(int i=0;i<produitslist.size();i++){
                        basketprice=basketprice+(produitslist.get(i).getPrice()*produitslist.get(i).getQuantite());
                    }
                    totalprice.setText("$ "+String.valueOf(basketprice));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(myfragment.getContext(), "Checkout continued", Toast.LENGTH_SHORT).show();
                        basketprice=0;
                        totalprice.setText("$ "+String.valueOf(basketprice));
                    }
                });
            }
        });

















        return myfragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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