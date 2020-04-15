package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class SplashScreenActivity extends AppCompatActivity {

    final int SPLASH_SCREEN_TIMEOUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

       Runnable runnable = new Runnable() {
           @Override
           public void run() {
               Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
               startActivity(intent);
               finish();
           }
       };

       new Handler().postDelayed(runnable , this.SPLASH_SCREEN_TIMEOUT);

    }
}
