package com.devcrawlers.wesselni;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Offer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ProfileFragment extends Fragment {


    // EditText fields
    EditText first_name, last_name, phone, email, adresse, city, zip;
    Spinner account_type;
    ImageView profileImg,valid_icon;
    TextView nameTextView, type_accountTextView,titleOfOffers,valid_text_view;
    String authenticatedUserId;
    Button update_button;

    int numberOfOffers = 0, numberOfRequests = 0;
    TextView value_offers;
    String acT;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        adresse = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        nameTextView = view.findViewById(R.id.full_name);
        type_accountTextView = view.findViewById(R.id.type_of_account);
        titleOfOffers = view.findViewById(R.id.titleOfOffers);
        valid_text_view = view.findViewById(R.id.valid_text_view);
        account_type = view.findViewById(R.id.account_type);
        zip = view.findViewById(R.id.zip);
        valid_icon = view.findViewById(R.id.valid_icon);
        profileImg = view.findViewById(R.id.profile_img);
        value_offers = view.findViewById(R.id.value_offers);
        update_button = view.findViewById(R.id.update_button);
        update_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Voullez vous vraiment modifer vos données ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Oui",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(email.getText().toString().isEmpty() || first_name.getText().toString().isEmpty()
                                           || last_name.getText().toString().isEmpty() || city.getText().toString().isEmpty() ||
                                        adresse.getText().toString().isEmpty() || zip.getText().toString().isEmpty() ){
                                            Toast.makeText(getContext(),"Veuillez remplir tout les champs SLV !",Toast.LENGTH_SHORT).show();
                                        }else {
                                            if(!email.getText().toString().trim().matches(emailPattern)) {
                                                Toast.makeText(getContext(), "Tapez une adresse email valide !", Toast.LENGTH_SHORT).show();
                                            }else if(!(PhoneNumberUtils.isGlobalPhoneNumber(phone.getText().toString()) && phone.getText().length()==10)){
                                                Toast.makeText(getContext(), "Tapez un numéro de téléphone valide !", Toast.LENGTH_SHORT).show();
                                            }else{
                                                saveModifications();
                                            }
                                        }
                                    }
                                });
                        builder1.setNegativeButton(
                                "Nom",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
        );
        getAuthentifcatedUserInfo();
        return view;
    }

    // This function is for retrieving user info from DB
    public void getAuthentifcatedUserInfo() {
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url, Provider.profileUrl,
                DataConnection.Method.GET, DataConnection.Header.TEXT) {
            @Override
            public void before() {
            }

            @Override
            public void after() {
            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("----------------------------------------Respanse Tag :", reponse);

                try {


                    JSONObject jsonObject = new JSONObject(reponse);
                    JSONObject userObject = jsonObject.getJSONObject("user");
                    authenticatedUserId = userObject.getString("id");
                    Picasso.get().load(Provider.publicUrl + userObject.getString("avatar")).into(profileImg);
                    Log.wtf("link", Provider.publicUrl + userObject.getString("avatar"));
                    first_name.setText(userObject.getString("first_name"));
                    Log.wtf("haaaaaaaaahowa : ", userObject.getString("first_name"));
                    String fName = userObject.getString("first_name");
                    String lName = userObject.getString("last_name");
                    nameTextView.setText(fName.toUpperCase().charAt(0) + fName.substring(1, fName.length()) + " " + lName.toUpperCase().charAt(0) + lName.substring(1, lName.length()));
                    last_name.setText(userObject.getString("last_name"));
                    email.setText(userObject.getString("email"));
                    adresse.setText(userObject.getString("adresse"));
                     acT = userObject.getString("account_type");
                    type_accountTextView.setText(acT.toUpperCase().charAt(0) + acT.substring(1, acT.length()));
                    zip.setText(userObject.getString("zip_code"));
                    phone.setText(userObject.getString("phone"));
                    if(userObject.getString("state").equals("NV")){
                        valid_text_view.setText("Invalide");
                        valid_icon.setImageResource(R.drawable.employee);
                    }
                    ArrayAdapter arrayAdapter;
                    String acType = userObject.getString("account_type");
                    acType = acType.toUpperCase().charAt(0) + acType.substring(1, acType.length());
                    if (userObject.getString("account_type").equals("Conducteur")) {
                        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                        new String[]{acType, "Passager"});
                    }
                    else
                    {
                        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                                new String[]{acType, "Conducteur"});
                    }

                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    account_type.setAdapter(arrayAdapter);
                    city.setText(userObject.getString("city"));
                    getNumberOfOffers();
                    getNumberOfRequests();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("aaaaach had l9lawi a anas ", e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Log.wtf("hello", error);
            }
        };
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        Log.wtf("my token ", token);
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
    }

    // If the acccount type of the connected user is "Conducteur" we use this function to calculate the number of offers of this user
    public void getNumberOfOffers() {
        List<Integer> nbrOffers = new ArrayList<>();
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url, Provider.offerSubUrl,
                DataConnection.Method.GET, DataConnection.Header.TEXT) {

            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                try {
                    JSONArray jsonArrayOffers = new JSONArray(reponse);
                    JSONObject sj;
                    int count = 0;
                    for (int i = 0; i < jsonArrayOffers.length(); i++) {
                        sj = jsonArrayOffers.getJSONObject(i);

                        if (sj.getString("user_id").equals(authenticatedUserId)) {
                            numberOfOffers++;
                            Log.wtf("value of number ", String.valueOf(numberOfOffers));
                        }
                    }
                    if(acT.equals("Conducteur") || acT.equals("conducteur")){
                        value_offers.setText(String.valueOf(numberOfOffers));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {

            }
        };
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        Log.wtf("my token ", token);
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
        Log.wtf("cddddddddddddddd", String.valueOf(numberOfOffers));
    }

    // This function is used to save the new modifications
    public void saveModifications() {
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url, Provider.usersSubUrl+authenticatedUserId,
                DataConnection.Method.PUT, DataConnection.Header.JSON) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Vos données sont bien modifiés !");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getAuthentifcatedUserInfo();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }


            @Override
            public void onError(String error) {

            }


        };
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        Log.wtf("my token ", token);
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.addParams("first_name" , first_name.getText().toString());
        dataConnection.addParams("last_name" , last_name.getText().toString());
        dataConnection.addParams("adresse" , adresse.getText().toString());
        dataConnection.addParams("zip_code" , zip.getText().toString());
        dataConnection.addParams("phone",phone.getText().toString());
        dataConnection.addParams("email" , email.getText().toString());
        dataConnection.addParams("city" , city.getText().toString());
        dataConnection.addParams("account_type", account_type.getSelectedItem().toString());
        dataConnection.startConnection();
    }

    public void getNumberOfRequests(){
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url, Provider.requestSubUrl,
                DataConnection.Method.GET, DataConnection.Header.TEXT) {

            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                try {
                    JSONArray jsonArrayOffers = new JSONArray(reponse);
                    JSONObject sj;
                    int count = 0;
                    for (int i = 0; i < jsonArrayOffers.length(); i++) {
                        sj = jsonArrayOffers.getJSONObject(i);

                        if (sj.getString("user_id").equals(authenticatedUserId)) {
                            numberOfRequests++;
                        }
                    }
                    Log.wtf("act from requests" , acT);
                    if(acT.equals("Passager") || acT.equals("passager")){
                        titleOfOffers.setText("Demandes");
                        value_offers.setText(String.valueOf(numberOfRequests));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {

            }
            };
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        Log.wtf("my token ", token);
        dataConnection.addAppHearder("Authorization", "Bearer " + token);

        dataConnection.startConnection();
        Log.wtf("cddddddddddddddd", String.valueOf(numberOfOffers));
    }
}
