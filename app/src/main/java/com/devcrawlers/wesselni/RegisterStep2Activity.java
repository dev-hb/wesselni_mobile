package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterStep2Activity extends AppCompatActivity {
    LinearLayout step1, step2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
    }

    public void doSendVerificationCode(View view){
        // send code via sms here

        step1.setVisibility(View.GONE);
        step2.setVisibility(View.VISIBLE);
    }

    public void doValidateAccount(View view){
        Toast.makeText(this, "Validating account...", Toast.LENGTH_LONG).show();
    }

}
