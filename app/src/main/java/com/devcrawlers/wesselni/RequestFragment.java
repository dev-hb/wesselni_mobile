package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Offer;
import com.devcrawlers.wesselni.entities.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RequestFragment extends Fragment {
    private ListView listViewRequests;
    private ArrayList<Request> requestArrayList;
    private FloatingActionButton floatingActionButton;
    private EditText searchStartCity;
    private EditText searchTargetCity;
    ProgressBar progressBar;
    private int authUserId=-1;

    public RequestFragment() {
        // Required empty public constructor
    }
    public RequestFragment(int authUserId) {
        this.authUserId=authUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_request, container, false);

        progressBar = (ProgressBar) myInflater.findViewById(R.id.progressBarRequest);
        listViewRequests = (ListView) myInflater.findViewById(R.id.listtViewRequest);
        searchStartCity=myInflater.findViewById(R.id.searchStartCity);
        searchTargetCity=myInflater.findViewById(R.id.searchTargetCity);
        searchStartCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRequests(searchStartCity.getText().toString(),searchTargetCity.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchTargetCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchRequests(searchStartCity.getText().toString(),searchTargetCity.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                if(authUserId==-1){
                    progressBar.setVisibility(View.GONE);
                    searchStartCity.setVisibility(View.VISIBLE);
                    searchTargetCity.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("Respanse Tag :", reponse);
                try {
                    //JSONObject jsonObjectoffres = new JSONObject(reponse);
                    JSONArray jsonArrayRequests = new JSONArray(reponse);
                    JSONObject sj;
                    Request re;
                    for (int i = 0; i < jsonArrayRequests.length(); i++) {
                        sj = jsonArrayRequests.getJSONObject(i);
                        Date sdt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("startdateTime"));
                        Date edt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sj.getString("enddateTime"));
                        re=new Request(sj.getInt("id"), sj.getJSONObject("startcity").getString("name"),
                                sj.getJSONObject("targetcity").getString("name"), sdt,edt,sj.getInt("state"),
                                 sj.getInt("user_id"), sj.getInt("nb_place"));
                        if(authUserId!=-1){
                            if(re.getUser_id()==authUserId){
                                requestArrayList.add(re);
                            }
                        }else{
                            requestArrayList.add(re);
                        }
                    }
                    RequestAdabter requestAdabter=new RequestAdabter(requestArrayList,getActivity());
                    listViewRequests.setAdapter(requestAdabter);
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
    public void searchRequests(String keyWrdStart,String keyWrdtarget){
        ArrayList<Request> requests=new ArrayList<>();
        Request requestKey=new Request();
        requestKey.setStartCity(keyWrdStart);
        requestKey.setTargetCity(keyWrdtarget);
        for(Request re : requestArrayList){
            if(re.comper(requestKey)){
                requests.add(re);
            }
        }
        RequestAdabter requestAdabter=new RequestAdabter(requests,getActivity());
        listViewRequests.setAdapter(requestAdabter);

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
