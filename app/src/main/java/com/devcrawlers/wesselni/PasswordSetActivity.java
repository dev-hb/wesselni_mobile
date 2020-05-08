package com.devcrawlers.wesselni;

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

import androidx.appcompat.app.AppCompatActivity;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;

import org.json.JSONObject;

import java.util.StringTokenizer;

public class PasswordSetActivity extends AppCompatActivity {
    EditText pwd1, pwd2, emailfield;
    String email, name, photo, fname, lname;
    Button btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_set);
        Intent intent = this.getIntent();
        email = intent.getExtras().getString("email");
        name = intent.getExtras().getString("name");
        photo = intent.getExtras().get("photo").toString();

        btn = findViewById(R.id.setPassButton);
        progressBar = findViewById(R.id.progressBarSetPassword);

        emailfield = findViewById(R.id.myemail);
        emailfield.setText(email);

        StringTokenizer stringTokenizer = new StringTokenizer(name, " ");
        if(stringTokenizer.countTokens() > 1){
            fname = stringTokenizer.nextToken();
            lname = stringTokenizer.nextToken();
        }else{
            fname = stringTokenizer.nextToken();
            lname = "";
        }

        pwd1 = findViewById(R.id.pwd1);
        pwd2 = findViewById(R.id.pwd2);
    }

    public void doPasswordSet(View view){
        String spwd1, spwd2;
        spwd1 = this.pwd1.getText().toString();
        spwd2 = this.pwd2.getText().toString();

        if(spwd1.equals(spwd2)){
            sendPassword(spwd1, spwd2);
        }else{
            Toast.makeText(this, "Les deux mots de passe ne sont pas identique", Toast.LENGTH_LONG).show();
        }
    }

    private void sendPassword(String pwd1, String pwd2) {
        DataConnection dataConnection=new DataConnection(this, Provider.updatePwd) {
            @Override
            public void before() {
                btn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void after() {
                btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                try{
                    JSONObject jsonObjectRespance = new JSONObject(reponse);
                    if(jsonObjectRespance.getBoolean("success")==true){
                        //i save the token in the Preferances..
                        saveToken(jsonObjectRespance.getString("token"));

                        SharedPreferences sharedPref = getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
                        String token=sharedPref.getString("token","null");
                        Toast.makeText(PasswordSetActivity.this,"You connected sexesFully"+token,Toast.LENGTH_LONG).show();

                        Intent it=new Intent(PasswordSetActivity.this, MainActivity.class);
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

                        Toast.makeText(getApplicationContext(), "Erreuuuur", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error de Connextion",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        };
        dataConnection
                .addParams("email", email)
                .addParams("password", pwd1)
                .addParams("avatar", photo)
                .addParams("first_name", fname)
                .addParams("last_name", lname);
        dataConnection.startConnection();
    }

    private void saveToken(String token) {
        SharedPreferences sharedPref = getSharedPreferences("tokenRef",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.commit();
    }

}
