package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class testAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SharedPreferences sharedPref = getSharedPreferences("tokenRef",Context.MODE_PRIVATE);
    String token=sharedPref.getString("token","null");
        Toast.makeText(testAct.this,"You connected sexesFully"+token,Toast.LENGTH_LONG).show();;


}
}
