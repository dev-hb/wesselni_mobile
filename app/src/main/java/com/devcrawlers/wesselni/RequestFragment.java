package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RequestFragment extends Fragment {
    private ListView listViewOffre;
    private ArrayList<Request> requestArrayList;
    private FloatingActionButton floatingActionButton;
    ProgressBar progressBar;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_request, container, false);

        progressBar = (ProgressBar) myInflater.findViewById(R.id.progressBarRequest);
        listViewOffre = (ListView) myInflater.findViewById(R.id.listtViewRequest);
        requestArrayList=new ArrayList<>();
        getRequests();

        floatingActionButton=(FloatingActionButton) myInflater.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new AddRequestFragment());
                fragmentTransaction.commit();
            }
        });

        return myInflater;
    }

    public void getRequests() {

        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.requestSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
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
                    JSONArray jsonArrayRequests = new JSONArray(reponse);
                    JSONObject sj;
                    for (int i = 0; i < jsonArrayRequests.length(); i++) {
                        sj = jsonArrayRequests.getJSONObject(i);
                        Date sdt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("startdateTime"));
                        Date edt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("enddateTime"));
                        requestArrayList.add(new Request(sj.getInt("id"), sj.getJSONObject("startcity").getString("name"),
                                sj.getJSONObject("targetcity").getString("name"), sdt,edt,sj.getInt("state"),
                                 sj.getInt("user_id"), sj.getInt("nb_place")));
                    }
                    RequestAdabter requestAdabter=new RequestAdabter(requestArrayList,getActivity());
                    listViewOffre.setAdapter(requestAdabter);
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


class RequestAdabter extends BaseAdapter {

    ArrayList<Request> requestArrayList;
    Context context;

    public RequestAdabter(ArrayList<Request> requests,Context c) {
        requestArrayList=requests;
        context =c;
    }

    @Override
    public int getCount() {
        return requestArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Request r=requestArrayList.get(position);
        view=LayoutInflater.from(context).inflate(R.layout.request_row, null);


        ((TextView)view.findViewById(R.id.textViewCitys_row_request)).setText(r.getStartCity()+" a "+r.getTargetCity());
        //((TextView) view.findViewById(R.id.textViewAddress_row_request)).setText(r.get());
        ((TextView) view.findViewById(R.id.textViewDateTime_row_request)).setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(r.getStartdateTime()) +" a "+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(r.getEnddateTime()));
        return view;
    }
}
