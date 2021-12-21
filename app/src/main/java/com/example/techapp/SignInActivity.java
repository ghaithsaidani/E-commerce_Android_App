package com.example.techapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.forgot_password.Forgot_pass_email;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText edittextmail,edittextpassword;
    private FirebaseAuth auth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_sign_in);

        edittextmail=findViewById(R.id.mailedittext);
        edittextpassword=findViewById(R.id.passwordedittext);
        Button signInButton =findViewById(R.id.signin);
        TextView signUp=findViewById(R.id.signup);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(
                        R.anim.left_slide_in, R.anim.right_slide_out);

            }
        });
        TextView forgot=(TextView)findViewById(R.id.forgot_pass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(), Forgot_pass_email.class);
                startActivity(i);
                overridePendingTransition(
                        R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });

        auth=FirebaseAuth.getInstance();
        progressbar=findViewById(R.id.progressbar);


    }

    private void login() {
        String email=edittextmail.getText().toString().trim();
        String password=edittextpassword.getText().toString().trim();

        if(email.isEmpty()){
            edittextmail.setError("Email Required");
            edittextmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edittextmail.setError("Please write a valid mail!");
            edittextmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edittextpassword.setError("Password Required");
            edittextpassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressbar.setVisibility(View.GONE);
                    Intent i=new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(
                            R.anim.top_slide_in, R.anim.bottom_slide_out);
                }
                else{
                    Toast.makeText(SignInActivity.this,"Sign in failed",Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });


    }


}