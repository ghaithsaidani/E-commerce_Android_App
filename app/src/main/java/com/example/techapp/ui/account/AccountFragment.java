package com.example.techapp.ui.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.techapp.R;
import com.example.techapp.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    View view;
    private TextView username,adresse;
    private Button editProfile, logout;
    CircleImageView image;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        username = view.findViewById(R.id.UserName);
        editProfile = view.findViewById(R.id.EditProfile);
        logout = view.findViewById(R.id.logout);
        image = view.findViewById(R.id.userimage);
        adresse=view.findViewById(R.id.adresse);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username1 = snapshot.child("username").getValue().toString().trim();
                username.setText(username1);
                String adress = snapshot.child("adresse").getValue().toString();
                adresse.setText(adress);
                String imagurl=snapshot.child("image").getValue().toString();
                Picasso.get().load(imagurl).into(image);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_account_to_navigation_editeprofile);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), SignInActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(
                        R.anim.bottom_slide_in, R.anim.top_slide_out);
            }
        });


        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}