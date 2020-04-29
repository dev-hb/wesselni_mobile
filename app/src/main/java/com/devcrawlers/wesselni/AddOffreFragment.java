package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.channels.DatagramChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.microedition.khronos.egl.EGLDisplay;


public class AddOffreFragment extends Fragment {

    private EditText editTextAdress,editTextPrix,editTextDate,editTextTime;
    public static EditText editTextPosition;
    private Spinner spinnerStartCity,spinnerTargetCity;
    private Button buttonAddOffre;
    private ImageButton imageButtonPosition;
    private ArrayList<City> arrayListCitys;

    public AddOffreFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_offre, container, false);
        spinnerStartCity=view.findViewById(R.id.spinnerStarCity);
        spinnerTargetCity=view.findViewById(R.id.spinnerTargetCity);
        editTextAdress=view.findViewById(R.id.editTextAdress);
        editTextDate=view.findViewById(R.id.editTextDate);
        editTextTime=view.findViewById(R.id.editTextTime);
        editTextPrix=view.findViewById(R.id.editTextPrix);
        editTextPosition=view.findViewById(R.id.editTextPosition);
        editTextPosition.setEnabled(false);
        arrayListCitys=new ArrayList<>();
        buttonAddOffre=view.findViewById(R.id.buttonAddOffre);
        imageButtonPosition=(ImageButton) view.findViewById(R.id.imageButtonPosition);
        imageButtonPosition.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), MapsActivity.class);
            getActivity().startActivity(intent);
        });
        buttonAddOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOffre();
            }
        });
        getCitys();
        return view;
    }



    public void getCitys(){
        DataConnection dataConnection=new DataConnection(getActivity(), Provider.url,Provider.citysSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("citys----",reponse);
                try{
                    JSONObject jsonObjectCitys=new JSONObject(reponse);
                    JSONArray jsonArray=jsonObjectCitys.getJSONArray("cities");
                    JSONObject js;
                    for(int i=0;i<jsonArray.length();i++){
                        js=jsonArray.getJSONObject(i);
                        arrayListCitys.add(new City(js.getLong("id"),js.getString("name"),js.getLong("people")));

                    }
                    ArrayAdapter<City> cityArrayAdapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,arrayListCitys);
                    cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStartCity.setAdapter(cityArrayAdapter);
                    spinnerTargetCity.setAdapter(cityArrayAdapter);


                }catch (Exception e){

                }
            }

            @Override
            public void onError(String error) {

            }
        } ;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
    }


    public void addOffre(){
        DataConnection dataConnection=new DataConnection(getActivity(),Provider.url,Provider.offerSubUrl,DataConnection.Method.POST) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("respance",reponse);
            }

            @Override
            public void onError(String error) {
                Log.wtf("rs",error);
            }
        };
        Date dt=new Date(editTextDate.getText().toString()+" "+editTextTime.getText().toString());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Log.wtf("dlsml",simpleDateFormat.format(dt));
        dataConnection.addParams("nbplace",5+"")
                .addParams("dateTime",simpleDateFormat.format(dt))
                .addParams("prix",editTextPrix.getText().toString())
                .addParams("targetcity",((City)spinnerTargetCity.getSelectedItem()).getId()+"")
                .addParams("startcity",((City)spinnerStartCity.getSelectedItem()).getId()+"")
                .addParams("address",editTextAdress.getText().toString())
                .addParams("position",editTextPosition.getText().toString());
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new OffreFragment());
        fragmentTransaction.commit();
    }



}
