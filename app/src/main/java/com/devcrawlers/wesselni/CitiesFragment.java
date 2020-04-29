package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.City;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class CitiesFragment extends Fragment {
    private ListView listViewCities;
    private ArrayList<City> citiesArrayList;
    ProgressBar progressBarCities;
    SearchView searchView;
    CitiesAdapter citiesAdapter;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_cities, container, false);


        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        this.token = sharedPref.getString("token", null);

        listViewCities = myInflater.findViewById(R.id.listCities);
        progressBarCities = myInflater.findViewById(R.id.progressBarCities);
        citiesArrayList=new ArrayList<>();
        getCities();

        searchView = myInflater.findViewById(R.id.search_cities);
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<City> arr = (ArrayList<City>) citiesArrayList.stream()
                        .filter(city -> city.getName().toLowerCase().contains(newText.toLowerCase()))
                        .collect(Collectors.toList());
                citiesAdapter.setCitiesArrayList(arr);
                citiesAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return myInflater;
    }

    public void getCities() {

        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.citysSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
            @Override
            public void before() {

            }

            @Override
            public void after() {
                progressBarCities.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                try {
                    JSONArray jsonArray = new JSONArray(reponse);
                    JSONObject sj;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        sj = jsonArray.getJSONObject(i);
                        citiesArrayList.add(
                                new City(
                                        sj.getLong("id"),
                                        sj.getString("name"),
                                        sj.getLong("people"),
                                        sj.getString("description"),
                                        0,
                                        0
                                )
                        );
                    }
                    citiesAdapter=new CitiesAdapter(citiesArrayList,getActivity());
                    listViewCities.setAdapter(citiesAdapter);
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

    class CitiesAdapter extends BaseAdapter {

        ArrayList<City> citiesArrayList;
        Context context;

        public CitiesAdapter(ArrayList<City> cities,Context c) {
            citiesArrayList=cities;
            context =c;
        }

        @Override
        public int getCount() {
            return citiesArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return citiesArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            City city=citiesArrayList.get(position);
            view=LayoutInflater.from(context).inflate(R.layout.cities_row, null);

            ((TextView)view.findViewById(R.id.citiesRowName)).setText(city.getName());
            ((TextView) view.findViewById(R.id.citiesRowHab)).setText(new Long(city.getPoaple()).toString()+" hab");
            String starttarget = new Long(city.getStart()).toString()+" départs | "+ new Long(city.getTarget()).toString()
                    +" arrivées";
            ((TextView) view.findViewById(R.id.citiesRowLast)).setText(starttarget);
            ImageView cityImage=(ImageView) view.findViewById(R.id.citiesRowImage);
            view.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new CityFragment(city));
                fragmentTransaction.commit();
            });
            return view;
        }

        public void setCitiesArrayList(ArrayList<City> arr){
            this.citiesArrayList = arr;
        }
    }
}



