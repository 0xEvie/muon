package com.github.zeroxevie.muon.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.zeroxevie.muon.Activities.Activities.Add_LoginRecord_Activity;

import com.github.zeroxevie.muon.Adapters.LoginRecordsRecyclerViewAdapter;
import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Objects.LoginRecord;
import com.github.zeroxevie.muon.Objects.PlatformRecord;
import com.github.zeroxevie.muon.R;
import io.realm.RealmResults;

public class View_Platform_Record_Activity extends AppCompatActivity
{

    DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_platform_record);
        dbManager = new DBManager(getApplicationContext());
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        final PlatformRecord platformRecord = dbManager.getRealm().where(PlatformRecord.class).equalTo("id", id).findFirst();
        setTitle("Viewing: "+platformRecord.getPlatform_title());

        RealmResults<LoginRecord> loginRecord = dbManager.getRealm().where(LoginRecord.class).equalTo("platform_title", platformRecord.getPlatform_title()).findAll();

        RecyclerView rv = findViewById(R.id.loginRecordList);
        LoginRecordsRecyclerViewAdapter loginRecordAdapter = new LoginRecordsRecyclerViewAdapter(loginRecord);
        rv.setAdapter(loginRecordAdapter);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                llm.getOrientation());
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(View_Platform_Record_Activity.this, Add_LoginRecord_Activity.class);
                intent.putExtra("platform_title", platformRecord.getPlatform_title());
                intent.putExtra("platform_type", platformRecord.getPlatform_type());
                startActivity(intent);
            }
        });
    }
}
