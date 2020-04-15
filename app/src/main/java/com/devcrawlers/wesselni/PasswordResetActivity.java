package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class PasswordResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
    }

    public void doResetPassword(){
        //: proccess password reset here
    }

    public void goLogin(View view){
        this.finish();
    }
}
