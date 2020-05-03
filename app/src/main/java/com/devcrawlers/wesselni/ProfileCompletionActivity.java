package com.devcrawlers.wesselni;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileCompletionActivity extends AppCompatActivity {
    LinearLayout etape1, etape2;
    private ImageView imagecin;
    private ImageButton avatar;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_LOAD_AVATAR = 2;
    private EditText nom, prenom, adresse, telephone;
    String nom_user, prenom_user, adresse_user, tele_user;
    Spinner spinner;
    final CharSequence[] options = {"Prendre une photo", "Choisir une photo", "Annuler"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_completion);
        etape1 = findViewById(R.id.etape1);
        etape2 = findViewById(R.id.etape2);
        avatar = findViewById(R.id.imagetoupload);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        adresse = findViewById(R.id.adresse);
        telephone = findViewById(R.id.telephone);
        imagecin = findViewById(R.id.imagecin);
        spinner = findViewById(R.id.spinner1);
        String[] items = new String[]{"Casablanca", "Rabat", "Tanger"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //spinner.setAdapter(adapter);

    }

    //afficher la boite de dialogue pour la CIN

    public void uploadCin(View v) {
        //Intent galleryIntent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir votre carte d'identit�");

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

    //afficher la boite de dialogue pour la photo de profil
    public void uploadAvatar(View v) {
        //Intent galleryIntent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(galleryIntent,RESULT_LOAD_AVATAR);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisie votre photo de profil");

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

    //passer
    public void Suivant(View v) {
        etape1.setVisibility(View.GONE);
        etape2.setVisibility(View.VISIBLE);
        nom_user = nom.getText().toString();
        prenom_user = prenom.getText().toString();
        adresse_user = adresse.getText().toString();
        tele_user = telephone.getText().toString();
    }

    //revenie
    public void Precedent(View v) {
        etape2.setVisibility(View.GONE);
        etape1.setVisibility(View.VISIBLE);
    }

    public void Terminer(View v) {
        //: end operation
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        avatar.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imagecin.setImageBitmap(selectedImage);
                    }
                    break;
                case 3:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imagecin.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
}


