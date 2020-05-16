package com.devcrawlers.wesselni;

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
import com.devcrawlers.wesselni.entities.Car;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class CarsFragment extends Fragment {
    private ListView listViewCars;
    private ArrayList<Car> carArrayList;
    private FloatingActionButton floatingActionButton;
    ProgressBar progressBar;

    public CarsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_cars, container, false);

        progressBar = (ProgressBar) myInflater.findViewById(R.id.progressBarCar);
        listViewCars = (ListView) myInflater.findViewById(R.id.listtViewCar);
        carArrayList=new ArrayList<>();
        getCars();

        floatingActionButton=(FloatingActionButton) myInflater.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new AddCarFragment());
                fragmentTransaction.commit();
            }
        });

        return myInflater;
    }

    public void getCars() {

        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.carsSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
            @Override
            public void before() {

            }

            @Override
            public void after() {
                    progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("Respanse Tag :", reponse);
                try {
                    //JSONObject jsonObjectoffres = new JSONObject(reponse);
                    JSONArray jsonArraycars = new JSONArray(reponse);
                    JSONObject sj;
                    Car car;
                    for (int i = 0; i < jsonArraycars.length(); i++) {
                        sj = jsonArraycars.getJSONObject(i);
                        carArrayList.add(new Car(sj.getInt("id"), sj.getString("idNumber"),sj.getString("mark"),
                                sj.getString("model"),sj.getString("color"), sj.getInt("nb_place"),
                                 sj.getString("picture"),sj.getString("re_card"),sj.getInt("state")==1?true:false,sj.getInt("user_id")));

                    }
                    CarAdabter carAdabter=new CarAdabter(carArrayList,getActivity());
                    listViewCars.setAdapter(carAdabter);
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


class CarAdabter extends BaseAdapter {

    ArrayList<Car> carsArrayList;
    Context context;

    public CarAdabter(ArrayList<Car> cars,Context c) {
        carsArrayList=cars;
        context =c;
    }

    @Override
    public int getCount() {
        return carsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return carsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Car car=carsArrayList.get(position);
        view=LayoutInflater.from(context).inflate(R.layout.car_row, null);
        ImageView picture=view.findViewById(R.id.pathPicture_car);
        Picasso.get().load(Provider.publicUrl + car.getPathPicture()).into(picture);
        ((TextView)view.findViewById(R.id.textViewIdNumber_row_car)).setText(car.getIdNumber());
        ((TextView)view.findViewById(R.id.textViewMarkModel_row_car)).setText(car.getMark()+" "+car.getModel());
        ((TextView)view.findViewById(R.id.textViewNbPlace_row_car)).setText(car.getNbPlace()+"");

        return view;
    }
}
