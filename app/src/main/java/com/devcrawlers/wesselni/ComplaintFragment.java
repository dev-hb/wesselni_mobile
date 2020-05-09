package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.entities.Complaint;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComplaintFragment extends Fragment {

    private ListView listViewComplaints;
    private ArrayList<Complaint> complaintsArrayList;
    private FloatingActionButton floatingActionButton;
    ProgressBar progressBar;
    private int authUserId=-1;

    public ComplaintFragment(int authUserId) {
        this.authUserId=authUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflater = inflater.inflate(R.layout.fragment_complaint, container, false);

        progressBar = (ProgressBar) myInflater.findViewById(R.id.progressBarComplaint);
        listViewComplaints = (ListView) myInflater.findViewById(R.id.listtViewComplaint);

        complaintsArrayList=new ArrayList<>();
        getComplaints();

        //floatingActionButton=(FloatingActionButton) myInflater.findViewById(R.id.floatingActionButton);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment,new AddRequestFragment());
//                fragmentTransaction.commit();
//            }
//        });

        return myInflater;
    }

    public void getComplaints() {

        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url,Provider.complaintsSubUrl, DataConnection.Method.GET,DataConnection.Header.TEXT) {
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
                    JSONArray jsonArrayComplaints = new JSONArray(reponse);
                    JSONObject sj;
                    Complaint co;
                    for (int i = 0; i < jsonArrayComplaints.length(); i++) {
                        sj = jsonArrayComplaints.getJSONObject(i);
                        co=new Complaint(sj.getInt("id"), sj.getInt("user_id"),
                                sj.getString("email"), sj.getString("fullName"),
                                sj.getString("subject"), sj.getString("message"));

                            if(co.getUser_id()==authUserId){
                                complaintsArrayList.add(co);
                            }
                    }
                    ComplaintAdapter complaintAdapter=new ComplaintAdapter(complaintsArrayList,getActivity());
                    listViewComplaints.setAdapter(complaintAdapter);
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
