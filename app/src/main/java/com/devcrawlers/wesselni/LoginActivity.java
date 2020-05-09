package com.devcrawlers.wesselni;

import androidx.annotation.NonNull;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLogin,editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextLogin= findViewById(R.id.email);
        editTextPassword= findViewById(R.id.password);
        buttonLogin= findViewById(R.id.btnLogin);
        progressBar= findViewById(R.id.progressBar);

        editTextLogin.setText("zakaria.hba.97@gmail.com");
        editTextPassword.setText("hba7222000");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            mGoogleSignInClient.signOut();
        }

        SignInButton signInButtonWithGoole = findViewById(R.id.sign_in_with_google);
        signInButtonWithGoole.setOnClickListener(v -> signInWithGoogle());

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount account = completedTask.getResult();
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        String email = account.getEmail();
        DataConnection dataConnection=new DataConnection(this, Provider.checkUserExistance) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
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
                        Intent intent = new Intent(LoginActivity.this, PasswordSetActivity.class);

                        intent.putExtra("email", email);
                        intent.putExtra("name", account.getDisplayName());
                        intent.putExtra("photo", account.getPhotoUrl());

                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "Créer votre compte", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this,"Error de Connextion",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        };
        dataConnection.addParams("email", email);
        dataConnection.startConnection();
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
