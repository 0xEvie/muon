package com.github.zeroxevie.muon.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.zeroxevie.muon.Activities.Welcome_Activity;

import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Objects.LoginRecord;
import com.github.zeroxevie.muon.R;

public class Add_LoginRecord_Activity extends AppCompatActivity
{
    private TextInputEditText platformTitleField;
    private EditText usernameField;
    private EditText passwordField;
    private Spinner typeSpinner;
    private Button addButton;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_login_record);
        Intent incomingIntent = getIntent();
        String platform_title = incomingIntent.getStringExtra("platform_title");
        String platform_type = incomingIntent.getStringExtra("platform_type");


        platformTitleField = findViewById(R.id.platformTitleField);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.signInPasswordField);
        typeSpinner = findViewById(R.id.platformTypeSpinner);
        addButton = findViewById(R.id.addButton);

        dbManager = new DBManager(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_platform_record_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord(v);
            }
        });

        if (platform_title!=null && platform_type!=null)
        {
            platformTitleField.setText(platform_title);

            switch(platform_type)
            {
                case "Website":
                    typeSpinner.setSelection(0);
                    break;
                case "Application":
                    typeSpinner.setSelection(1);
                    break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_secondary, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.lockButton:
                Intent intent = new Intent(Add_LoginRecord_Activity.this, Welcome_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addRecord(View v)
    {
        try
        {
            String platform_title = platformTitleField.getText().toString();
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String platform_type = typeSpinner.getSelectedItem().toString();


            if (username != null & password != null & dbManager.validateWebsite(platform_title) & dbManager.validateLoginRecord(username, password))
            {

                LoginRecord loginRecord = dbManager.getRealm().where(LoginRecord.class).equalTo("platform_title", platform_title).and().equalTo("username", username).findFirst();

                if (loginRecord != null)
                {
                    Toast.makeText(this, "Duplicate Login record", Toast.LENGTH_LONG)
                            .show();
                } else
                {
                    //Add Website Record
                    dbManager.addLogin(platform_title, platform_type, username, password);
                    Intent addIntent = new Intent(Add_LoginRecord_Activity.this, MainActivity.class);
                    startActivity(addIntent);
                    finish();
                }
            } else
            {
                Toast.makeText(this, "Error Adding record", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (Exception e)
        {
            Log.e("Password_Manager", e.getMessage());
        }
    }

}
