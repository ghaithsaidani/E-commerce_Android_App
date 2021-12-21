package com.example.techapp.forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.R;
import com.example.techapp.SignInActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_pass_email extends AppCompatActivity {


    private FirebaseAuth auth;
    private String email;
    private Button sendmail;
    private EditText mail;
    private TextView gotosms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_email);
        sendmail=findViewById(R.id.send_mail);
        mail=findViewById(R.id.forgotmailedittext);
        gotosms=findViewById(R.id.sms);
        auth=FirebaseAuth.getInstance();
        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=mail.getText().toString();
                if(email.isEmpty()){
                    mail.setError("Required an email");
                }
                else{
                    forgotpass();
                }
            }
        });

        gotosms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forgot_pass_email.this,Forgot_pass_number.class));
                overridePendingTransition(
                        R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });



    }
    private void forgotpass(){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Forgot_pass_email.this,"check your email",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Forgot_pass_email.this, SignInActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Forgot_pass_email.this,"Error "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}