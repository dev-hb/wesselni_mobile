package com.devcrawlers.wesselni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devcrawlers.wesselni.entities.Offer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;


public class OffreDetails extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Offer offer;



    public OffreDetails(Offer offer) {
        // Required empty public constructor
        this.offer=offer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_offre_details, container, false);
        ((TextView) view.findViewById(R.id.textViewCitys)).setText(offer.getStartCity()+" a "+offer.getTargetCity());
       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        ((TextView) view.findViewById(R.id.textViewDateTime)).setText(simpleDateFormat.format(offer.getDateTime()));
        ((TextView) view.findViewById(R.id.textViewPrix)).setText(offer.getPrix()+"DH");
        ((ImageView) view.findViewById(R.id.imageViewState)).setColorFilter(offer.isState()==true? Color.parseColor("#2ecc71"):Color.parseColor("#e74c3c"));
        ((TextView) view.findViewById(R.id.textViewAdress)).setText(offer.getAddrese());
        ((Button) view.findViewById(R.id.buttonPay)).setVisibility(offer.isState()==true? View.VISIBLE:View.GONE);
        ((TextView) view.findViewById(R.id.textViewNbpl)).setText(offer.getNbPlace()+" place");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Places.initialize(getActivity().getApplicationContext(),"AIzaSyBskA1YBZfvl9PQXmtFzRHMozuG9jCmZM0");
        ((Button) view.findViewById(R.id.buttonPay)).setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,new ReservationFragment(offer));
            fragmentTransaction.commit();
        });


        return view;
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        StringTokenizer stringTokenizer=new StringTokenizer(offer.getLoglat(), ",");
        double lat=Double.parseDouble(stringTokenizer.nextToken());
        double log=Double.parseDouble(stringTokenizer.nextToken());
        LatLng sydney = new LatLng(lat, log);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

    }



}
