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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Offer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.Inflater;


public class OffreFragment extends Fragment {
    private ListView listViewOffre;
    private ArrayList<Offer> offerArrayList;

    public OffreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_offre, container, false);
        listViewOffre = (ListView) myInflater.findViewById(R.id.listtViewOffre);
        offerArrayList=new ArrayList<>();
        getOffres();
        return myInflater;
    }

    public void getOffres() {

        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.offerSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("Respanse Tag :", reponse);
                try {
                    //JSONObject jsonObjectoffres = new JSONObject(reponse);
                    JSONArray jsonArrayOffers = new JSONArray(reponse);
                    JSONObject sj;
                    for (int i = 0; i < jsonArrayOffers.length(); i++) {
                        sj = jsonArrayOffers.getJSONObject(i);
                        Date dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("dateTime"));
                        offerArrayList.add(new Offer(sj.getInt("id"), sj.getJSONObject("startcity").getString("name"),
                                sj.getJSONObject("targetcity").getString("name"), sj.getString("address"), dt,
                                false, sj.getInt("user_id"), sj.getInt("nbplace"),
                                sj.getString("latLong")));
                    }
                    OfferAdabter offerAdabter=new OfferAdabter(offerArrayList,getActivity());
                    listViewOffre.setAdapter(offerAdabter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                Log.wtf("error tag", error);
            }
        };
        Context context;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();


    }


}


class OfferAdabter extends BaseAdapter {

    ArrayList<Offer> offerArrayList;
    Context context;

    public OfferAdabter(ArrayList<Offer> offers,Context c) {
        offerArrayList=offers;
        context =c;
    }

    @Override
    public int getCount() {
        return offerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return offerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Offer f=offerArrayList.get(position);
        view=LayoutInflater.from(context).inflate(R.layout.offre_row, null);


        ((TextView)view.findViewById(R.id.textViewCitys_row_offre)).setText(f.getStartCity()+" a "+f.getTargetCity());
        ((TextView) view.findViewById(R.id.textViewAddress_row_offre)).setText(f.getAddrese());
        ((TextView) view.findViewById(R.id.textViewDateTime_row_offre)).setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(f.getDateTime()));
        return view;
    }
}