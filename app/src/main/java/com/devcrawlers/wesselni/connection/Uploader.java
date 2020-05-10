package com.devcrawlers.wesselni.connection;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Uploader {

    private Context context;
    private String fileData = null; // file data here
    private String subFolder;

    public Uploader(Context context, String fileData) {
        this.context = context;
        this.fileData = fileData;
        this.subFolder = "";
    }

    public Uploader(Context context, String fileData, String subFolder) {
        this.context = context;
        this.fileData = fileData;
        this.subFolder = subFolder;
    }

    public Uploader start(){
        if(fileData == null){
            onUploadFailed("Please provide a file data to be uploaded");
            return null;
        }
        DataConnection dataConnection = new DataConnection(context, Provider.uploader) {
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
                    onUploadSuccess(jsonObject.getString("path"));
                } catch (JSONException e) {
                    onUploadFailed(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                onUploadFailed(error);
            }
        };
        SharedPreferences sharedPref = context.getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.addParams("filedata", fileData)
                .addParams("subfolder", subFolder);
        dataConnection.startConnection();

        return this;
    }

    public abstract void onUploadSuccess(String path);
    public abstract void onUploadFailed(String error);

}