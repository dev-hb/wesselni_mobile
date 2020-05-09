package com.devcrawlers.wesselni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Offer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class ReservationActivity extends AppCompatActivity {

    private Offer offer;

    private EditText editTextNbpalce;
    private Button buttonReserv;
    CardInputWidget cardInputWidget;
    private int authUserId=0;

    private ProgressDialog builder;

    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        offer= (Offer) getIntent().getSerializableExtra("offre");
        builder = new ProgressDialog(this);
        editTextNbpalce=(EditText) findViewById(R.id.editTextNbPlace);
        editTextNbpalce.setHint("il rest "+(offer.getNbPlace()-offer.getNbplaceResever())+" place dans cette voyage");
        buttonReserv=(Button) findViewById(R.id.buttonReserve);
        cardInputWidget=findViewById(R.id.stripeCardInputWidget);
        getUserId();
        buttonReserv.setOnClickListener(v -> {
            validete();
            clientPayment();
        });
    }

    public void validete(){
        if(Integer.parseInt(editTextNbpalce.getText().toString())>offer.getNbPlace()){
            editTextNbpalce.setError("il rest "+(offer.getNbPlace()-offer.getNbplaceResever())+" place dans cette voyage");
            return;
        }
        DataConnection dataConnection=new DataConnection(this, Provider.url,
                Provider.reservationsSubUrl,DataConnection.Method.POST,DataConnection.Header.JSON) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Toast.makeText(ReservationActivity.this,"votre reservation est bine effctuer",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String error) {
                Log.wtf("jjskd",error);
            }
        };
        SharedPreferences sharedPref = getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.addParams("offre_id",offer.getId()+"")
                .addParams("nbplace",editTextNbpalce.getText().toString());
        dataConnection.startConnection();

    }


    public void clientPayment(){
        DataConnection dataConnection=new DataConnection(this,Provider.paymentUrl,
                Provider.paymentSubUrl,DataConnection.Method.POST,DataConnection.Header.JSON) {
            @Override
            public void before() {
                builder.setCancelable(false);
                builder.setCanceledOnTouchOutside(false);
                builder.show();
            }

            @Override
            public void after() {
                builder.dismiss();
            }

            @Override
            public void onFinish(String reponse) {
                try {
                    Log.wtf("hello",reponse);
                    JSONObject strypeJson=new JSONObject(reponse);
                    String stripePublishableKey = strypeJson.getString("publishableKey");
                    String paymentIntentClientSecret = strypeJson.getString("clientSecret");
                    PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                    stripe = new Stripe(
                            getApplicationContext(),
                            Objects.requireNonNull(stripePublishableKey)
                    );
                    if (params != null) {
                        ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                                .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                        stripe.confirmPayment(ReservationActivity.this, confirmParams);
                    }
                }catch (Exception e){
                    Log.wtf("heloo","hello");
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.wtf("test-----",error);
            }
        };

        int amount=((int)(offer.getPrix()*Integer.parseInt(editTextNbpalce.getText().toString())*100));

        dataConnection.addParams("amount",amount+"");
        dataConnection.startConnection();


    }

    public void getUserId(){
        DataConnection dataConnection=new DataConnection(this,Provider.url,Provider.profileUrl,DataConnection.Method.GET,DataConnection.Header.JSON) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                try{
                    JSONObject jsonObject=new JSONObject(reponse);
                    JSONObject user=jsonObject.getJSONObject("user");
                    authUserId=jsonObject.getJSONObject("user").getInt("id");
                    //  AuthUser=new User(authUserId,user.getString("firs_name"),user.getString("last_name"),user.getString("email"),user.getString("phone"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        };
        SharedPreferences sharedPref = getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message,
                              boolean restartDemo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        if (restartDemo) {
            builder.setPositiveButton("ok",
                    (DialogInterface dialog, int index) -> {
                        finish();
                    });
        } else {
            builder.setPositiveButton("Ok", null);
        }
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("salam al3alm","-----------------------------");
        // Handle the result of stripe.confirmPayment

        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));

    }

    public static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<ReservationActivity> activityRef;

        PaymentResultCallback(@NonNull ReservationActivity activity) {
            activityRef = new WeakReference<>(activity);

        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final ReservationActivity activity = activityRef.get();
            Log.wtf("heloos","hello from ...");
            if (activity == null) {

                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        "Votre payment est bien effectuer",
                        true
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        "votre payment est non pas effectuer",
                        false
                );
            }

        }

        @Override
        public void onError(@NonNull Exception e) {
            final ReservationActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.wtf("helooe","hello from ...");
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString(), false);
        }
    }
}
