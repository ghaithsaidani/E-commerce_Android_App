package com.example.techapp.forgot_password;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Forgot_pass_number extends AppCompatActivity {

    private FirebaseAuth auth;
    private String number;
    private Button sendsms;
    private EditText phone;
    private TextView gotoemail;
    private ProgressBar numprogress;
    private PhoneAuthOptions options;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_number);
        auth=FirebaseAuth.getInstance();
        sendsms=findViewById(R.id.send_sms);
        phone=findViewById(R.id.forgotnumberedittext);
        gotoemail=findViewById(R.id.emailforgot);
        gotoemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forgot_pass_number.this,Forgot_pass_email.class));
                overridePendingTransition(
                        R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });



        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number=phone.getText().toString().trim();
                if(number.isEmpty()){
                    phone.setError("Required a Phone number");
                }
                else{
                    forgotpass();
                }
            }
        });
        numprogress=findViewById(R.id.numprogress);
        auth = FirebaseAuth.getInstance();
    }

    private void forgotpass() {
        numprogress.setVisibility(View.VISIBLE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                numprogress.setVisibility(View.GONE);
                Toast.makeText(Forgot_pass_number.this,"Code sent",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                numprogress.setVisibility(View.GONE);
                Toast.makeText(Forgot_pass_number.this,e.getMessage(),Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                numprogress.setVisibility(View.GONE);
                Intent i=new Intent(Forgot_pass_number.this, Code_Verif.class);
                i.putExtra("mobile",phone.getText().toString());
                i.putExtra("verification",verificationId);
                startActivity(i);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+216"+phone.getText().toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
}