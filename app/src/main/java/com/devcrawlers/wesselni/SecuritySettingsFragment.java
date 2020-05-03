package com.devcrawlers.wesselni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SecuritySettingsFragment extends Fragment {
    Button validatePasswordChange;
    EditText old_password1, old_password2, new_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_security, container,false);

        old_password1 = view.findViewById(R.id.old_password1);
        old_password2 = view.findViewById(R.id.old_password2);
        new_password = view.findViewById(R.id.new_password);

        validatePasswordChange = view.findViewById(R.id.validate);
        validatePasswordChange.setOnClickListener(v -> validatePassword(
                old_password1.getText().toString(),
                old_password2.getText().toString(),
                new_password.getText().toString()));

        return view; 
    }

    private void validatePassword(String oldp1, String oldp2, String newp) {
        if(new Helpers().empty_strings(oldp1, oldp2, newp)){
            Toast.makeText(getContext(), "Svp de remplir tous les champs", Toast.LENGTH_LONG).show();
            return;
        }

        if(oldp1.equals(oldp2)){
            if(! oldp1.equals(newp)){

            }else{
                Toast.makeText(getContext(), "Vous ne pouvez pas utiliser le même mot de passe", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getContext(), "Les deux mots de passe n'ont pas identiques", Toast.LENGTH_LONG).show();
        }
    }
}
