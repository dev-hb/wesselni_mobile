package com.devcrawlers.wesselni;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devcrawlers.wesselni.entities.Offer;

import java.text.SimpleDateFormat;


public class OffreDetails extends Fragment {

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


        return view;
    }


}
