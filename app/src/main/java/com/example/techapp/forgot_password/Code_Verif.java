package com.example.techapp.forgot_password;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.techapp.MainActivity;
import com.example.techapp.R;

public class Code_Verif extends AppCompatActivity {

    private PinView pinview;
    private Button verifier_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verif);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        pinview=findViewById(R.id.pinview);
        pinview.setText(getIntent().getStringExtra("verification"));


        verifier_code=findViewById(R.id.verifier_code);
        verifier_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinview.getText().toString().equals(getIntent().getStringExtra("verification"))){
                    startActivity(new Intent(Code_Verif.this, MainActivity.class));
                }
                else {
                    Toast.makeText(Code_Verif.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}