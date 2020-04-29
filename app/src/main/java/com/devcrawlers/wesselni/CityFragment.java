package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;
import com.devcrawlers.wesselni.entities.Offer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CityFragment extends Fragment {

    private ListView listViewOffreStart, listViewOffreTarget;
    private ArrayList<Offer> offerArrayList;
    private ArrayList<Offer> offerArrayListTarget;
    TextView city_name, city_hab, city_start, city_target, city_description;
    ProgressBar progressBarStart, progressBarTarget;
    String token;
    City city;

    public CityFragment(City city){
        this.city = city;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_city, container, false);

        progressBarStart = view.findViewById(R.id.progressBarStart);
        listViewOffreStart = view.findViewById(R.id.listtViewOffreStart);
        progressBarTarget = view.findViewById(R.id.progressBarTarget);
        listViewOffreTarget = view.findViewById(R.id.listtViewOffreTarget);

        city_name = view.findViewById(R.id.city_name);
        city_hab = view.findViewById(R.id.city_hab);
        city_start = view.findViewById(R.id.city_start);
        city_target = view.findViewById(R.id.city_target);
        city_description = view.findViewById(R.id.city_description);

        city_name.setText(this.city.getName());
        city_hab.setText(new Long(this.city.getPoaple()).toString()+" hab");
        if(this.city.getDescription().length()>500)
            city_description.setText(this.city.getDescription().replaceAll("<br>", "").substring(0, 500)+" ...");
        else
            city_description.setText(this.city.getDescription().replaceAll("<br>", ""));

        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        this.token = sharedPref.getString("token", null);

        offerArrayList = new ArrayList<>();
        offerArrayListTarget = new ArrayList<>();
        getLastStartCity();
        getLastTargetCity();

        view.findViewById(R.id.city_more_info).setOnClickListener(v -> moreInfo());

        return view;
    }

    public void moreInfo(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://fr.wikipedia.com/wiki/"+city.getName()));
        startActivity(intent);
    }

    public void getLastStartCity(){
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.offerSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
            @Override
            public void before() {

            }

            @Override
            public void after() {
                progressBarStart.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                try {
                    JSONArray jsonArrayOffers = new JSONArray(reponse);
                    JSONObject sj;
                    for (int i = 0; i < jsonArrayOffers.length(); i++) {
                        sj = jsonArrayOffers.getJSONObject(i);
                        Date dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("dateTime"));
                        offerArrayList.add(new Offer(sj.getInt("id"), sj.getJSONObject("startcity").getString("name"),
                                sj.getJSONObject("targetcity").getString("name"), sj.getString("address"), dt,
                                sj.getInt("state")==1?true:false, sj.getInt("user_id"), sj.getInt("nbplace"),
                                sj.getString("latLong"),sj.getDouble("prix")));
                    }
                    CityFragment.OfferAdabter offerAdabter=new CityFragment.OfferAdabter(offerArrayList,getActivity());
                    listViewOffreStart.setAdapter(offerAdabter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                Log.wtf("error tag", error);
            }
        };
        dataConnection.addAppHearder("Authorization", "Bearer " + this.token);
        dataConnection.startConnection();
    }

    public void getLastTargetCity(){
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.offerSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
            @Override
            public void before() {

            }

            @Override
            public void after() {
                progressBarTarget.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                try {
                    JSONArray jsonArrayOffers = new JSONArray(reponse);
                    JSONObject sj;
                    for (int i = 0; i < jsonArrayOffers.length(); i++) {
                        sj = jsonArrayOffers.getJSONObject(i);
                        Date dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("dateTime"));
                        offerArrayListTarget.add(new Offer(sj.getInt("id"), sj.getJSONObject("startcity").getString("name"),
                                sj.getJSONObject("targetcity").getString("name"), sj.getString("address"), dt,
                                sj.getInt("state")==1?true:false, sj.getInt("user_id"), sj.getInt("nbplace"),
                                sj.getString("latLong"),sj.getDouble("prix")));
                    }
                    CityFragment.OfferAdabter offerAdabter=new CityFragment.OfferAdabter(offerArrayListTarget,getActivity());
                    listViewOffreTarget.setAdapter(offerAdabter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.wtf("error tag", error);
            }
        };
        dataConnection.addAppHearder("Authorization", "Bearer " + this.token);
        dataConnection.startConnection();
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


            ((TextView)view.findViewById(R.id.textViewCitys_row_offre)).setText(f.getStartCity()+"  a  "+f.getTargetCity());
            ((TextView) view.findViewById(R.id.textViewAddress_row_offre)).setText(f.getAddrese());
            ((TextView) view.findViewById(R.id.textViewDateTime_row_offre)).setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(f.getDateTime()));
            ImageView imageViewStaet=(ImageView) view.findViewById(R.id.imageView_row_offre);
            imageViewStaet.setColorFilter(f.isState()==true? Color.parseColor("#2ecc71"):Color.parseColor("#e74c3c"));
            view.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new OffreDetails(f));
                fragmentTransaction.commit();
            });
            return view;
        }
    }

}
