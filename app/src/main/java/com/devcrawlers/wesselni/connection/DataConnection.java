package com.devcrawlers.wesselni.connection;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class DataConnection {

    //Class Enum...
    //Connection Methode.
    public enum Method {
        ;
        public static final int GET = Request.Method.GET;
        public static final int POST = Request.Method.POST;
        public static final int PUT = Request.Method.PUT;
    }

    //data type.
    public enum Header {
        ;
        public final static String JSON = "application/json";
        public final static String TEXT = "application/text";
    }


    private String URL;
    private Context context;
    private int methode;
    private HashMap<String, String> params;//to sand data in url after the '?'
    private HashMap<String, String> AppHeader;//to sand data in the body of the requist
    private String header;//data header text or json
    private StringRequest stringRequest;
    private JsonObjectRequest jsonObjectRequest;

    public DataConnection(Context context, String subURL) {
        this.URL = Provider.url + subURL;
        this.context = context;
        params = new HashMap<>();
        methode = Method.POST;
        header = Header.JSON;
        AppHeader = new HashMap<>();
    }

    public DataConnection(Context context, String subURL, int methode) {
        this.URL = Provider.url + subURL;
        this.context = context;
        params = new HashMap<>();
        this.methode = methode;
        header = Header.JSON;
        AppHeader = new HashMap<>();
    }

    public DataConnection(Context context, String url, String subURL) {
        this.URL = url + subURL;
        this.context = context;
        params = new HashMap<>();
        methode = Method.POST;
        header = Header.JSON;
        AppHeader = new HashMap<>();
    }

    public DataConnection(Context context, String url, String subURL, int method) {
        this.URL = url + subURL;
        this.context = context;
        params = new HashMap<>();
        this.methode = method;
        header = Header.JSON;
        AppHeader = new HashMap<>();
    }

    public DataConnection(Context context, String url, String subURL, int method, String header) {
        this.URL = url + subURL;
        this.context = context;
        params = new HashMap<>();
        this.methode = method;
        this.header = header;
        AppHeader = new HashMap<>();
    }


    public void startConnection() {
        if (header == Header.TEXT) {
            stringRequest = new StringRequest(methode, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onFinish(response);
                    after();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onError(error.getMessage());
                    after();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    AppHeader.put("Content-Type", "application/json; charset=utf-8");
                    return AppHeader;
                }
            };
            before();
            Volley.newRequestQueue(context).add(stringRequest);
        }
        if (header == Header.JSON) {
            jsonObjectRequest = new JsonObjectRequest(methode, URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            onFinish(response.toString());
                            after();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("----> Error " , error.getMessage());
                            if(error.networkResponse!=null){

                                onError(new String(error.networkResponse.data));
                            }else{
                                onError(error.getMessage());
                            }

                            after();
                        }
                    }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    AppHeader.put("Content-Type", "application/json; charset=utf-8");
                    return AppHeader;
                }


            };
            before();
            Volley.newRequestQueue(context).add(jsonObjectRequest);
        }


    }


    public void setHeader(String header) {
        this.header = header;
    }

    public DataConnection addParams(String key, String val) {
        params.put(key, val);
        return this;
    }

    public DataConnection addAppHearder(String key, String val) {
        AppHeader.put(key, val);
        return this;
    }

    //befor connection is start this method will execute
    public abstract void before();

    //affter Connection is finish this methode will execute
    public abstract void after();

    //when connection is finish this method will execute
    public abstract void onFinish(String reponse);

    //when an error this methode will execute
    public abstract void onError(String error);
}
