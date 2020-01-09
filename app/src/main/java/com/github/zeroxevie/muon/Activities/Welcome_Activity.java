package com.github.zeroxevie.muon.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;

import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Fragments.SignIn_Fragment;
import com.github.zeroxevie.muon.Fragments.SignUp_Fragment;
import com.github.zeroxevie.muon.R;

public class Welcome_Activity extends AppCompatActivity
{
    DBManager dbManager;
    private Button resetPasswordButton;
    private String screenToDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        dbManager = new DBManager(this);
        MobileAds.initialize(this, "ca-app-pub-2106873641296345~6930375364");

        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        if (!dbManager.userExists())
        {
            changeFragment(new SignUp_Fragment());
        } else
        {
            changeFragment(new SignIn_Fragment());
        }

    }

    public void changeFragment(Fragment fragmentToChangeTo)
    {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragmentToChangeTo);
        transaction.commit();
    }

}
