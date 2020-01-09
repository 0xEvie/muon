package com.github.zeroxevie.muon.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import com.github.zeroxevie.muon.Activities.Activities.MainActivity;
import com.github.zeroxevie.muon.Activities.Welcome_Activity;
import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Objects.UserCredentials;
import com.github.zeroxevie.muon.R;

public class SignIn_Fragment extends Fragment implements View.OnClickListener
{

    DBManager dbManager;
    ImageButton signInButton;
    TextView passwordField;
    Button resetPasswordButton;

    public SignIn_Fragment()
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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Context thisContext = view.getContext();
        dbManager = new DBManager(thisContext);
        signInButton = view.findViewById(R.id.signInButton);
        passwordField = view.findViewById(R.id.signInPasswordField);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        signInButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);

    }
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.signInButton:
                String password = passwordField.getText().toString();
                if (dbManager.validatePassword(password))
                {
                    UserCredentials userCredentialsToFind = dbManager.getRealm().where(UserCredentials.class).findFirst();

                    if (Objects.requireNonNull(userCredentialsToFind).getPassword().equals(password))
                    {
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Password Incorrect", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.resetPasswordButton:
            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog);

            builder.setTitle("Reset Password")
                    .setMessage("Resetting your vault's password will DELETE all of the records stored in the vault. Do you want to continue?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dbManager.deleteAll();
                            Intent intent = new Intent(getActivity(),Welcome_Activity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            break;
        }
    }
}
