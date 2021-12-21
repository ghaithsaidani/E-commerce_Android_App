package com.example.techapp.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.techapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {

    View myfragment;
    EditText username,email,password,confirmpassword,birthday;
    Button update;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myfragment=inflater.inflate(R.layout.fragment_edit_profile, container, false);

        username =myfragment.findViewById(R.id.UserNameEdit);
        email =myfragment.findViewById(R.id.EmailEdit);
        password =myfragment.findViewById(R.id.PasswordEdit);
        confirmpassword=myfragment.findViewById(R.id.ConfirmPasswordEdit);
        birthday =myfragment.findViewById(R.id.BirthdayEdit);

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker datePicker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select date")
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .build();

                datePicker.show(getChildFragmentManager(),"Date Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        birthday.setText(datePicker.getHeaderText());
                    }
                });

            }
        });
        update =myfragment.findViewById(R.id.Update);




        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username1 = snapshot.child("username").getValue().toString().trim();
                String Email1 = snapshot.child("email").getValue().toString().trim();
                String birthday1 = snapshot.child("birthday").getValue().toString().trim();
                String password1=snapshot.child("password").getValue().toString().trim();
                username.setText(username1);
                email.setText(Email1);
                password.setText(password1);
                birthday.setText(birthday1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username=username.getText().toString().trim();
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                String Confirmpassword=confirmpassword.getText().toString().trim();
                String Birthday=birthday.getText().toString().trim();
                //String phonenumber=edittextphone.getText().toString().trim();

                if(Username.isEmpty()){
                    username.setError("Username Required");
                    username.requestFocus();
                    return;
                }

                if(Email.isEmpty()){
                    email.setError("Email Required");
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("Please write a valid mail!");
                    email.requestFocus();
                    return;
                }

                if(Password.isEmpty()){
                    password.setError("Password Required");
                    password.requestFocus();
                    return;
                }



                if(Confirmpassword.isEmpty()){
                    confirmpassword.setError("Password confirmation Required");
                    confirmpassword.requestFocus();
                    return;
                }

                if(!Confirmpassword.equals(Password)){
                    confirmpassword.setError("Password confirmation Incorrect");
                    confirmpassword.requestFocus();
                    return;
                }

                if(Birthday.isEmpty()){
                    birthday.setError("Birthdate Required");
                    birthday.requestFocus();
                    return;
                }
                else{
                    reference.child("username").setValue(username.getText().toString());
                    reference.child("password").setValue(password.getText().toString());
                    reference.child("email").setValue(email.getText().toString());
                    reference.child("birthday").setValue(birthday.getText().toString());
                    Toast.makeText(getContext(),"Account Updated",Toast.LENGTH_LONG).show();



                }



            }
        });




        return myfragment;
    }
}