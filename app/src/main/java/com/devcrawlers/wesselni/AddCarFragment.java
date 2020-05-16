package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.devcrawlers.wesselni.connection.DataConnection;
import com.devcrawlers.wesselni.connection.Provider;
import com.devcrawlers.wesselni.connection.Uploader;
import yuku.ambilwarna.AmbilWarnaDialog;


import java.io.ByteArrayOutputStream;

import static android.app.Activity.*;

public class AddCarFragment extends Fragment {

    private ImageView pictureCar,reCardCar;
    private EditText idNumber, mark,model,color,nbPlace;
    private Button buttonAddCar,btnuploadPicture_car,btnuploadReCard_car;
    private String pathpictureCar,pathreCardCar;
    private Bitmap piccar,picrecard;
    final CharSequence[] options = {"Prendre une photo", "Choisir une photo", "Annuler"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_car, container, false);
        idNumber=view.findViewById(R.id.editTextIdNumber_car);
        mark=view.findViewById(R.id.editTextMark_car);
        model=view.findViewById(R.id.editTextModel_car);
        color=view.findViewById(R.id.editTextColor_car);
        nbPlace=view.findViewById(R.id.editTextNbPlace_car);
        pictureCar=view.findViewById(R.id.pictureCar);
        reCardCar=view.findViewById(R.id.reCardCar);
        btnuploadPicture_car=view.findViewById(R.id.uploadPicture_car);
        btnuploadReCard_car=view.findViewById(R.id.uploadReCard_car);
        buttonAddCar=view.findViewById(R.id.buttonAddCar);
        color.setOnClickListener(v -> {
            showColorPicker(color);
        });

        btnuploadPicture_car.setOnClickListener(v -> {
            uploadPictureCar();
        });

        btnuploadReCard_car.setOnClickListener(v -> {
            uploadReCardCar();
        });

        buttonAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadpictureCar();
            }
        });

        return view;
    }

    public void addCar(){



        if (idNumber.getText().toString().isEmpty()) {
            idNumber.setError("Marticule de votre Voiture");
            return;
        }
        if (mark.getText().toString().isEmpty()) {
            mark.setError("Marque de votre Voiture");
            return;
        }
        if (model.getText().toString().isEmpty()) {
            model.setError("Module de votre Voiture");
            return;
        }

        if (color.getText().toString().isEmpty()) {
            color.setError("selectionne couleur de votre Voiture");
            return;
        }
        if (Integer.parseInt(nbPlace.getText().toString())<1) {
            nbPlace.setError("Min c'est 1");
            return;
        }
        if (pathpictureCar==null){
            Log.wtf("probleme upload picture Car",pathpictureCar);
            return;
        }

        if (pathreCardCar==null){
            Log.wtf("probleme upload picture carte grise",pathreCardCar);
            return;
        }


        DataConnection dataConnection=new DataConnection(getActivity(), Provider.url,Provider.carsSubUrl,DataConnection.Method.POST) {
            @Override
            public void before() {


            }

            @Override
            public void after() {

            }

            @Override
            public void onFinish(String reponse) {
                Log.wtf("respance",reponse);
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment,new CarsFragment());
                fragmentTransaction.commit();
            }

            @Override
            public void onError(String error) {
                Log.wtf("rs",error);
            }
        };

        dataConnection.addParams("idNumber",idNumber.getText().toString())
                .addParams("mark",mark.getText().toString())
                .addParams("model",model.getText().toString())
                .addParams("color",color.getText().toString())
                .addParams("nbPlace",nbPlace.getText().toString())
                .addParams("picture",pathpictureCar)
                .addParams("re_card",pathreCardCar);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("tokenRef", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "kdk");
        dataConnection.addAppHearder("Authorization", "Bearer " + token);
        dataConnection.startConnection();

    }

    private void showColorPicker(EditText editcolor){
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Log.wtf("color",String.valueOf(color));
                editcolor.setText(String.valueOf(color));
                editcolor.setBackgroundColor(color);

            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });
        dialog.show();

    }

    public void uploadPictureCar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choisir votre voiture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("Prendre une photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[i].equals("Choisir une photo")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[i].equals("Annuler")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    public void uploadReCardCar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choisir Carte Grise");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("Prendre une photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 2);

                } else if (options[i].equals("Choisir une photo")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 3);

                } else if (options[i].equals("Annuler")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void uploadpictureCar(){

        BitmapDrawable drawable = (BitmapDrawable) pictureCar.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String file = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        Uploader upload= new Uploader(getContext(),file) {
            @Override
            public void onUploadSuccess(String path) {
                Log.wtf("path pathpictureCar",path);
                pathpictureCar =path;
                uploadreCardCar();
            }
            @Override
            public void onUploadFailed(String error) {
                pathpictureCar=null;
            }
        };

        upload.start();
    }

    private void uploadreCardCar(){
        BitmapDrawable drawable = (BitmapDrawable) reCardCar.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
//
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String file = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Uploader upload= new Uploader(getContext(),file) {
            @Override
            public void onUploadSuccess(String path) {
                Log.wtf("path pathreCardCar",path);
                pathreCardCar =path;
                addCar();
            }

            @Override
            public void onUploadFailed(String error) {
                pathreCardCar=null;
            }
        };
        upload.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        if(requestCode== RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            Uri selectedImage=data.getData();
        }
        if(requestCode== RESULT_LOAD_AVATAR && resultCode==RESULT_OK){
            Uri selectedImage=data.getData();
            avatar.setImageURI(selectedImage);
        }

        */

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        piccar=selectedImage;
                        pictureCar.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        pictureCar.setImageURI(selectedImage);
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                pictureCar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                piccar=BitmapFactory.decodeFile(picturePath);
//                                cursor.close();
//                            }
//                        }
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        picrecard=selectedImage;
                        reCardCar.setImageBitmap(selectedImage);
                    }
                    break;
                case 3:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        reCardCar.setImageURI(selectedImage);
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                reCardCar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                picrecard=BitmapFactory.decodeFile(picturePath);
//                                cursor.close();
//                            }
//                        }
                    }
                    break;
            }
        }
    }
}
