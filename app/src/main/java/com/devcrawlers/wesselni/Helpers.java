package com.devcrawlers.wesselni;

import android.widget.EditText;

public class Helpers {

    public static boolean empty_field(EditText...fields){
        for(EditText field: fields){
            if(field.getText().toString().equals("")) return true;
        } return false;
    }

}
