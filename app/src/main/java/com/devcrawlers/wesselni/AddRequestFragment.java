package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddRequestFragment extends Fragment {

    private EditText editTextNbPlace,editTextStartDate,editTextStartTime,editTextEndDate,editTextEndTime;
    private Spinner spinnerStartCity,spinnerTargetCity;
    private Button buttonAddRequest;
    private ArrayList<City> arrayListCitys;

    public AddRequestFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_request, container, false);
        spinnerStartCity=view.findViewById(R.id.spinnerStarCity);
        spinnerTargetCity=view.findViewById(R.id.spinnerTargetCity);
        editTextStartDate=view.findViewById(R.id.editTextStartDate);
        editTextStartTime=view.findViewById(R.id.editTextStartTime);
        editTextEndDate=view.findViewById(R.id.editTextEndDate);
        editTextEndTime=view.findViewById(R.id.editTextEndTime);
        editTextNbPlace=view.findViewById(R.id.editTextNbPlace);
        arrayListCitys=new ArrayList<>();
        buttonAddRequest=view.findViewById(R.id.buttonAddRequest);
        buttonAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest();
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


    public void addRequest(){
        DataConnection dataConnection=new DataConnection(getActivity(),Provider.url,Provider.requestSubUrl,DataConnection.Method.POST) {
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
        Date dts=new Date(editTextStartDate.getText().toString()+" "+editTextStartTime.getText().toString());
        Date dte=new Date(editTextEndDate.getText().toString()+" "+editTextEndTime.getText().toString());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //Log.wtf("dlsml",simpleDateFormat.format(dts));
        dataConnection.addParams("nb_place",editTextNbPlace.getText().toString())
                .addParams("startdateTime",simpleDateFormat.format(dts))
                .addParams("enddateTime",simpleDateFormat.format(dte))
                .addParams("targetcity",((City)spinnerTargetCity.getSelectedItem()).getId()+"")
                .addParams("startcity",((City)spinnerStartCity.getSelectedItem()).getId()+"");
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new RequestFragment());
        fragmentTransaction.commit();
    }



}
