package com.devcrawlers.wesselni;

import android.widget.EditText;

public class Helpers {

    public boolean empty_field(EditText...fields){
        for(EditText field: fields){
            if(field.getText().toString().equals("")) return true;
        } return false;
    }

    public boolean empty_strings(String...strings){
        for(String s: strings){
            if(s.equals("")) return true;
        } return false;
    }

}
