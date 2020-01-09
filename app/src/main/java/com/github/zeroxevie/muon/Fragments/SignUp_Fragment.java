package com.github.zeroxevie.muon.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import com.github.zeroxevie.muon.Activities.Activities.MainActivity;
import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.R;

public class SignUp_Fragment extends Fragment implements View.OnClickListener
{

    DBManager dbManager;
    ImageButton signUpButton;
    TextView passwordField;
    AdView adView;
    public SignUp_Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Context thisContext = view.getContext();
        dbManager = new DBManager(thisContext);
        signUpButton = view.findViewById(R.id.signUpButton);
        passwordField = view.findViewById(R.id.signUpPasswordField);
        signUpButton.setOnClickListener(this);

    }
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.signUpButton:
                String password = passwordField.getText().toString();
                if (dbManager.validatePassword(password))
                {
                    dbManager.createUserCredentials(password);
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
