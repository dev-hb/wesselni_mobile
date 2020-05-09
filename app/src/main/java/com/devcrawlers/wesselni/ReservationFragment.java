package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Offer;
import com.devcrawlers.wesselni.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.Objects;


public class ReservationFragment extends Fragment {

    private Offer offer;

    private EditText editTextNbpalce;
    private Button buttonReserv;
    CardInputWidget cardInputWidget;
    private int authUserId=0;

    private Stripe stripe;

    public ReservationFragment() {
        // Required empty public constructor
    }
    public ReservationFragment(Offer offer) {
        // Required empty public constructor
        this.offer=offer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_reservation, container, false);
        editTextNbpalce=(EditText) view.findViewById(R.id.editTextNbPlace);
        editTextNbpalce.setHint("il rest "+(offer.getNbPlace()-offer.getNbplaceResever())+" place dans cette voyage");
        buttonReserv=(Button) view.findViewById(R.id.buttonReserve);
        cardInputWidget=view.findViewById(R.id.stripeCardInputWidget);
        getUserId();
        buttonReserv.setOnClickListener(v -> {
            validete();
            clientPayment();
        });
        return view;
    }

    public void validete(){
        if(Integer.parseInt(editTextNbpalce.getText().toString())>offer.getNbPlace()){
            editTextNbpalce.setError("il rest "+(offer.getNbPlace()-offer.getNbplaceResever())+" place dans cette voyage");
            return;
        }
        DataConnection dataConnection=new DataConnection(getActivity(), Provider.url,
                Provider.reservationsSubUrl,DataConnection.Method.POST,DataConnection.Header.JSON) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Toast.makeText(getActivity(),"votre reservation est bine effctuer",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String error) {
                Log.wtf("jjskd",error);
            }
        };
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.addParams("offre_id",offer.getId()+"")
                .addParams("nbplace",editTextNbpalce.getText().toString());
        dataConnection.startConnection();

    }


    public void clientPayment(){
        DataConnection dataConnection=new DataConnection(getActivity(),Provider.paymentUrl,
                Provider.paymentSubUrl,DataConnection.Method.POST,DataConnection.Header.JSON) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

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
                            getActivity().getApplicationContext(),
                            Objects.requireNonNull(stripePublishableKey)
                    );
                    if (params != null) {
                        ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                                .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                        stripe.confirmPayment(getActivity(), confirmParams);
                        stripe.onPaymentResult(1111,getActivity().getIntent(),new PaymentResultCallback(ReservationFragment.this));

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
        DataConnection dataConnection=new DataConnection(getActivity(),Provider.url,Provider.profileUrl,DataConnection.Method.GET,DataConnection.Header.JSON) {
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message,
                              boolean restartDemo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message);
        if (restartDemo) {
            builder.setPositiveButton("Restart demo",
                    (DialogInterface dialog, int index) -> {
                        cardInputWidget.clear();
                        clientPayment();
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
        @NonNull private final WeakReference<ReservationFragment> activityRef;

        PaymentResultCallback(@NonNull ReservationFragment activity) {
            activityRef = new WeakReference<>(activity);

        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final ReservationFragment activity = activityRef.get();
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
                        gson.toJson(paymentIntent),
                        true
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(),
                        false
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final ReservationFragment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.wtf("helooe","hello from ...");
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString(), false);
        }
    }

}
