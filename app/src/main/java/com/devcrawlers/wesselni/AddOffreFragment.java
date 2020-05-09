package com.devcrawlers.wesselni;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.channels.DatagramChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;


public class AddOffreFragment extends Fragment {

    private EditText editTextAdress,editTextPrix,editTextDate,editTextTime,editTextNBplace;


    public static EditText editTextPosition;
    private Spinner spinnerStartCity,spinnerTargetCity;
    private Button buttonAddOffre;
    private ArrayList<City> arrayListCitys;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    public AddOffreFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_offre, container, false);
        editTextPosition=view.findViewById(R.id.editTextPosition);
        spinnerStartCity=view.findViewById(R.id.spinnerStarCity);
        spinnerTargetCity=view.findViewById(R.id.spinnerTargetCity);
        editTextAdress=view.findViewById(R.id.editTextAdress);
        editTextNBplace=view.findViewById(R.id.editTextNbPlace);
        editTextDate=view.findViewById(R.id.editTextDate);
        editTextTime=view.findViewById(R.id.editTextTime);
        editTextDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year=cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity()
                    ,android.R.style.Theme_Material_Dialog_NoActionBar_MinWidth,onDateSetListener,year,month,day);
            datePickerDialog.show();
        });
        onDateSetListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editTextDate.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };
        editTextTime.setOnClickListener(v -> {
            Calendar calendar=Calendar.getInstance();
            int h=calendar.get(Calendar.HOUR_OF_DAY);
            int m=calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity()
                    ,android.R.style.Theme_Material_Dialog_MinWidth,onTimeSetListener,h,m,true);
            timePickerDialog.show();
        });
        onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextTime.setText(hourOfDay+":"+minute);
            }
        };

        editTextPrix=view.findViewById(R.id.editTextPrix);
        arrayListCitys=new ArrayList<>();
        buttonAddOffre=view.findViewById(R.id.buttonAddOffre);

        editTextPosition.setOnClickListener(v -> {
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
                        arrayListCitys.add(new City(js.getLong("id"),js.getString("name"),js.getInt("people")));

                    }
                    ArrayAdapter<City> cityArrayAdapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,arrayListCitys);
                    cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStartCity.setAdapter(cityArrayAdapter);
                    spinnerTargetCity.setAdapter(cityArrayAdapter);


                }catch (Exception e){
                    e.printStackTrace();
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
                .addParams("position",editTextPosition.getText().toString())
                .addParams("nbplace",editTextNBplace.getText().toString());
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,new OffreFragment());
        fragmentTransaction.commit();
    }



}

