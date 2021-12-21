package com.example.techapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.modeles.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView signin;
    private TextInputEditText edittextbirthdate,edittextusername,edittextemail,edittextpassword,edittextconfirmpass,edittextphone;
    private FirebaseAuth auth;
    private ProgressBar progress;






    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        signin=findViewById(R.id.signin1);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(),SignInActivity.class);
                startActivity(i);
                overridePendingTransition(
                        R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });

        progress=findViewById(R.id.progressbar);
        edittextbirthdate=findViewById(R.id.dateedittext);
        edittextusername=findViewById(R.id.usernameedittext);
        edittextemail=findViewById(R.id.emailedittext);
        edittextpassword=findViewById(R.id.passedittext);
        edittextconfirmpass=findViewById(R.id.confirmpassedittext);

        edittextbirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker datePicker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select date")
                                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                                .build();

                datePicker.show(getSupportFragmentManager(),"Date Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        edittextbirthdate.setText(datePicker.getHeaderText());
                    }
                });

            }
        });
        Button signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser();
            }
        });

        auth=FirebaseAuth.getInstance();


    }

    private void registeruser(){
        String username=edittextusername.getText().toString().trim();
        String email=edittextemail.getText().toString().trim();
        String password=edittextpassword.getText().toString().trim();
        String confirmpassword=edittextconfirmpass.getText().toString().trim();
        String birthdate=edittextbirthdate.getText().toString().trim();
        String phonenumber=edittextphone.getText().toString().trim();

        if(username.isEmpty()){
            edittextusername.setError("Username Required");
            edittextusername.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edittextemail.setError("Email Required");
            edittextemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edittextemail.setError("Please write a valid mail!");
            edittextemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edittextpassword.setError("Password Required");
            edittextpassword.requestFocus();
            return;
        }



        if(confirmpassword.isEmpty()){
            edittextconfirmpass.setError("Password confirmation Required");
            edittextconfirmpass.requestFocus();
            return;
        }

        if(!confirmpassword.equals(password)){
            edittextconfirmpass.setError("Password confirmation Incorrect");
            edittextconfirmpass.requestFocus();
            return;
        }

        if(birthdate.isEmpty()){
            edittextbirthdate.setError("Birthdate Required");
            edittextbirthdate.requestFocus();
            return;
        }




        else{
            progress.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user=new User(username,email,password,Integer.parseInt(phonenumber),birthdate);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this,"Account Registred",Toast.LENGTH_LONG).show();
                                    progress.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(SignUpActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
                                    progress.setVisibility(View.GONE);
                                }

                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUpActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();
                        progress.setVisibility(View.GONE);
                    }

                }
            });
        }



    }

    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((SignUpActivity) mContext).getWindow()
                .getCurrentFocus().getWindowToken(), 0);
    }
}