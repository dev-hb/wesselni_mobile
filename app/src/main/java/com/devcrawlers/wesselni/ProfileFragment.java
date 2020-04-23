package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {


    // EditText fields
    EditText first_name, last_name , phone , email, adresse, city, zip, account_type;
    ImageView profileImg;

    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        email =  view.findViewById(R.id.email);
        phone =  view.findViewById(R.id.phone);
        adresse = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        account_type = view.findViewById(R.id.account_type);
        zip = view.findViewById(R.id.zip);
        profileImg = view.findViewById(R.id.profile_img);
        getAuthentifcatedUserInfo();
        return view;
    }

    // This function is for retrieving user info from DB
    public void getAuthentifcatedUserInfo(){
        DataConnection dataConnection = new DataConnection(getActivity(), Provider.url, Provider.profileUrl,
                DataConnection.Method.GET, DataConnection.Header.JSON) {
            @Override
            public void before() {

            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {

                try {
                    JSONObject jsonObject = new JSONObject(reponse);
                    JSONObject userObject = jsonObject.getJSONObject("user");
                    first_name.setText( userObject.getString("first_name"));
                    last_name.setText(userObject.getString("last_name"));
                    email.setText(userObject.getString("email"));
                    adresse.setText(userObject.getString("adresse"));
                    zip.setText(userObject.getString("zip_code"));
                    phone.setText(userObject.getString("phone"));
                    account_type.setText(userObject.getString("account_type"));
                    city.setText(userObject.getString("city"));

                    /*
                        This part is for Image profile
                        http://192.168.1.20:99/uploads/files/
                        profileImg.setImageResource(Provider.ressourcesUrl +"uploads/files/");
                    */

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("hello",e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                Log.wtf("hello",error);
            }
        };
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();
    }
}
