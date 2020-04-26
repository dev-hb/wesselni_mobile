package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin,editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextLogin=(EditText) findViewById(R.id.email);
        editTextPassword=(EditText) findViewById(R.id.password);
        buttonLogin=(Button) findViewById(R.id.btnLogin);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        editTextLogin.setText("anas.devriani@gmail.com");
        editTextPassword.setText("hacker 2018");


    }

    public void doLogin(View view){
        DataConnection dataConnection=new DataConnection(this,"login") {
            @Override
            public void before() {
                buttonLogin.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void after() {
                buttonLogin.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                //Log.wtf("the Tag ---------",reponse);
                try{
                    JSONObject jsonObjectRespance = new JSONObject(reponse);
                    if(jsonObjectRespance.getBoolean("success")==true){
                        //i save the token in the Preferances..
                       saveToken(jsonObjectRespance.getString("token"));

                       SharedPreferences sharedPref = getSharedPreferences("tokenRef",Context.MODE_PRIVATE);
                        String token=sharedPref.getString("token","null");
                        Toast.makeText(LoginActivity.this,"You connected sexesFully"+token,Toast.LENGTH_LONG).show();

                       Intent it=new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                try{
                    JSONObject jsonObject=new JSONObject(error);
                    if(jsonObject.getBoolean("q")== false){
                        editTextLogin.setError(jsonObject.getString("message"));
                    }else{
                        Toast.makeText(LoginActivity.this,"Error de Connextion",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        };
        dataConnection.addParams("email",editTextLogin.getText().toString())
                .addParams("password",editTextPassword.getText().toString());
        dataConnection.startConnection();
    }

    private void saveToken(String token) {
        SharedPreferences sharedPref = getSharedPreferences("tokenRef",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.commit();
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
