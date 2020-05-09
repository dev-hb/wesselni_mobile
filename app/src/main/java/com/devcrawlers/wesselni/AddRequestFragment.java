package com.devcrawlers.wesselni;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddRequestFragment extends Fragment {

    private EditText editTextNbPlace,editTextStartDate,editTextStartTime,editTextEndDate,editTextEndTime;
    private Spinner spinnerStartCity,spinnerTargetCity;
    private Button buttonAddRequest;
    private ArrayList<City> arrayListCitys;

    DatePickerDialog datePickerDialog;

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

        editTextStartDate.setOnClickListener(v -> {
            showDatePicker(editTextStartDate);
        });

        editTextEndDate.setOnClickListener(v -> {
                showDatePicker(editTextEndDate);
        });

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(editTextStartTime);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(editTextEndTime);
            }
        });

        buttonAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest();
            }
        });

        getCitys();
        return view;
    }

    private void showDatePicker(EditText date){
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(AddRequestFragment.this.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        date.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker(EditText time){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddRequestFragment.this.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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

        if ((City)spinnerStartCity.getSelectedItem()==null) {
            ((TextView)spinnerStartCity.getSelectedView()).setError("selectionne votre depart");
            return;
        }

        if ((City)spinnerTargetCity.getSelectedItem()==null) {
            ((TextView)spinnerTargetCity.getSelectedView()).setError("selectionne votre distination");
            return;
        }

        if (editTextStartDate.getText().toString().isEmpty()) {
            editTextStartDate.setError("selectionne date debut");
            return;
        }
        if (editTextStartTime.getText().toString().isEmpty()) {
            editTextStartTime.setError("selectionne heur debut");
            return;
        }
        if (editTextEndDate.getText().toString().isEmpty()) {
            editTextEndDate.setError("selectionne date fin");
            return;
        }

        if (editTextEndTime.getText().toString().isEmpty()) {
            editTextEndTime.setError("selectionne heur fin");
            return;
        }
        if (Integer.parseInt(editTextNbPlace.getText().toString())<1) {
            editTextNbPlace.setError("Min c'est 1");
            return;
        }


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
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new RequestFragment());
                fragmentTransaction.commit();
            }

            @Override
            public void onError(String error) {
                Log.wtf("rs",error);
            }
        };

        Date dts=new Date(editTextStartDate.getText().toString()+" "+editTextStartTime.getText().toString());
        Date dte=new Date(editTextEndDate.getText().toString()+" "+editTextEndTime.getText().toString());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf=new SimpleDateFormat("d/M/yyyy HH:mm");
        try {
            dte=sdf.parse(editTextEndDate.getText().toString()+" "+editTextEndTime.getText().toString());
            dts=sdf.parse(editTextStartDate.getText().toString()+" "+editTextStartTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.wtf("dlsml",simpleDateFormat.format(dts));
        Log.wtf("dleml",simpleDateFormat.format(dte));
        dataConnection.addParams("nb_place",editTextNbPlace.getText().toString())
                .addParams("startdateTime",simpleDateFormat.format(dts))
                .addParams("enddateTime",simpleDateFormat.format(dte))
                .addParams("targetcity",((City)spinnerTargetCity.getSelectedItem()).getId()+"")
                .addParams("startcity",((City)spinnerStartCity.getSelectedItem()).getId()+"");
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();

    }



}
