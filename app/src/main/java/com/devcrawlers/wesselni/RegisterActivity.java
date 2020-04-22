package com.devcrawlers.wesselni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devcrawlers.wesselni.connection.DataConnection;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    Button registerButton;
    ProgressBar progressBar;
    EditText cin, email, password, password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cin = (EditText) findViewById(R.id.cin);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);

        registerButton = (Button) findViewById(R.id.btnRegister);
        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);

    }

    public void doRegister(View view){
        DataConnection dataConnection = new DataConnection(this, "register") {
            @Override
            public void before() {
                registerButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void after() {
                registerButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                try{
                    JSONObject jsonObjectRespance = new JSONObject(reponse);
                    if(jsonObjectRespance.getBoolean("success")==true){

                        Toast.makeText(RegisterActivity.this,"Votre compte a été créé avec succès" ,Toast.LENGTH_LONG).show();
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
                        Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(RegisterActivity.this ,"Error de Connextion",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        dataConnection.addParams("email",email.getText().toString())
                .addParams("password",password.getText().toString())
                .addParams("cin",cin.getText().toString());

        if(! (new Helpers()).empty_field(cin, email, password, password2)){
            if(password.getText().toString().equals(password2.getText().toString())){
                dataConnection.startConnection();
            }else{
                Toast.makeText(this, "Les deux mots de passe ne sont pas identiques", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Merci de remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }

    public void goLogin(View view){
        this.finish();
    }
}
