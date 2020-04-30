package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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


public class ReservationFragment extends Fragment {

    private Offer offer;

    private EditText editTextNbpalce;
    private Button buttonReserv;

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
        editTextNbpalce.setHint("il rest "+offer.getNbPlace()+" place dans cette voyage");
        buttonReserv=(Button) view.findViewById(R.id.buttonReserve);
        buttonReserv.setOnClickListener(v -> {
            validete();
        });
        return view;
    }

    public void validete(){
        if(Integer.parseInt(editTextNbpalce.getText().toString())>offer.getNbPlace()){
            editTextNbpalce.setError("il rest "+offer.getNbPlace()+" place dans cette voyage");
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


}
