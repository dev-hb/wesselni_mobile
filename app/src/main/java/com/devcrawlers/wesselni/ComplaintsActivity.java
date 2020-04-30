package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.FragmentTransaction;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComplaintsActivity extends AppCompatActivity {
    private EditText fullName,email,subject,message;
    private Button submet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fullName=findViewById(R.id.ComplaintFullName);
        email=findViewById(R.id.ComplaintEmail);
        subject=findViewById(R.id.ComplaintSubject);
        message=findViewById(R.id.ComplaintMessage);
        submet=findViewById(R.id.buttonAddComplaint);
        submet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComplaint();
            }
        });
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }
    public void addComplaint(){



        if (fullName.getText().toString().isEmpty()) {
            fullName.setError("Votre Nome Complet");
            return;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Votre Email");
            return;
        }
        if (subject.getText().toString().isEmpty()) {
            subject.setError("Objet de Reclamation");
            return;
        }

        if (message.getText().toString().isEmpty()) {
            message.setError("Message de Reclamation");
            return;
        }


        DataConnection dataConnection=new DataConnection(ComplaintsActivity.this, Provider.url,Provider.complaintsSubUrl,DataConnection.Method.POST) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("respance",reponse);
                Toast.makeText(ComplaintsActivity.this,"Votre Reclamation Envoyer",Toast.LENGTH_LONG).show();

                Intent it=new Intent(ComplaintsActivity.this, MainActivity.class);
                startActivity(it);
            }

            @Override
            public void onError(String error) {
                Log.wtf("rs",error);
            }
        };


        dataConnection.addParams("email",email.getText().toString())
                .addParams("fullName",fullName.getText().toString())
                .addParams("subject",subject.getText().toString())
                .addParams("message",message.getText().toString());
        SharedPreferences sharedPref = ComplaintsActivity.this.getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();

    }

}
