package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void doLogin(View view){
        Log.d("info", "Button login pressed!");
    }

    public void goRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goPasswordReset(View view){
        Intent intent = new Intent(this, PasswordResetActivity.class);
        startActivity(intent);
    }
}
