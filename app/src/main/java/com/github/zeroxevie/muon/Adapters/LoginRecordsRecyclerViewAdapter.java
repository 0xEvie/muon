package com.github.zeroxevie.muon.Adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.zeroxevie.muon.Activities.Activities.MainActivity;
import com.github.zeroxevie.muon.DMBS.DBManager;
import com.github.zeroxevie.muon.Objects.LoginRecord;
import com.github.zeroxevie.muon.Objects.PlatformRecord;
import com.github.zeroxevie.muon.R;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class LoginRecordsRecyclerViewAdapter extends RealmRecyclerViewAdapter<LoginRecord, LoginRecordsRecyclerViewAdapter.MyViewHolder>
{
    DBManager dbManager;
    Context thisContext;


    public LoginRecordsRecyclerViewAdapter(RealmResults<LoginRecord> data)
    {
        super(data, true);
    }

    @Override
    public LoginRecordsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_login_record, parent, false);
        thisContext = parent.getContext();
        dbManager = new DBManager(thisContext);
        return new LoginRecordsRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LoginRecordsRecyclerViewAdapter.MyViewHolder holder, int position)
    {
        final LoginRecord obj = getItem(position);

            holder.usernameField.setText(obj.getUsername());
            holder.passwordField.setText(obj.getPassword());


            View.OnClickListener copyUsernameListener = new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    LoginRecord loginRecordToDelete = obj;

                    try
                    {
                        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context
                                .CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("username", holder.usernameField.getText());
                        assert clipboard != null;
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(v.getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT)
                                .show();
                    } catch (Exception e)
                    {
                        Toast.makeText(v.getContext(), "Error occurred when copying to clipboard", Toast
                                .LENGTH_SHORT).show();
                    }

                }
            };

            View.OnClickListener copyPasswordListener = new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    LoginRecord loginRecordToDelete = obj;

                    try
                    {
                        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context
                                .CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("password", holder.passwordField.getText());
                        assert clipboard != null;
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(v.getContext(), "Copied to Clipboard", Toast.LENGTH_SHORT)
                                .show();
                    } catch (Exception e)
                    {
                        Toast.makeText(v.getContext(), "Error occurred when copying to clipboard", Toast
                                .LENGTH_SHORT).show();
                    }

                }
            };

            View.OnClickListener deleteLoginListener = new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    LoginRecord loginRecordToDelete = obj;

                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(thisContext, android.R.style.Theme_Material_Dialog);

                    builder.setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String platform_title = obj.getPlatform_title();
                                    String platform_type = obj.getPlatform_type();

                                    dbManager.deleteLogin(obj.getId());
                                    PlatformRecord platformRecord = dbManager.getRealm().where(PlatformRecord.class).equalTo("platform_title", platform_title).and().equalTo("platform_type", platform_type).findFirst();

                                    if (platformRecord==null)
                                    {
                                        Intent intent = new Intent(thisContext, MainActivity.class);
                                        thisContext.startActivity(intent);
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            };

            View.OnClickListener saveChanges = new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    LoginRecord loginRecord = obj;
                    String id = obj.getId();
                    String newUsername = holder.usernameField.getText().toString();
                    String newPassword = holder.passwordField.getText().toString();

                    dbManager.editRecord(id, newUsername, newPassword);

                    if (dbManager.validateLoginRecord(newUsername, newPassword))
                    {
                        dbManager.editRecord(id, newUsername, newPassword);
                    } else
                    {
                        Toast.makeText(v.getContext(), "Invalid String", Toast
                                .LENGTH_LONG).show();
                    }
                }
            };

            holder.copyUsername.setOnClickListener(copyUsernameListener);
            holder.copyPassword.setOnClickListener(copyPasswordListener);
            holder.saveChangesButton.setOnClickListener(saveChanges);
            holder.deleteButton.setOnClickListener(deleteLoginListener);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextInputEditText usernameField;
        public TextInputEditText passwordField;
        public ImageButton copyUsername;
        public ImageButton copyPassword;
        public Button saveChangesButton;
        public Button deleteButton;

        MyViewHolder(View view)
        {
            super(view);
            usernameField = view.findViewById(R.id.usernameField);
            passwordField = view.findViewById(R.id.signInPasswordField);
            copyUsername = view.findViewById(R.id.copyUsername);
            copyPassword = view.findViewById(R.id.copyPassword);
            saveChangesButton = view.findViewById(R.id.saveChangesButton);
            deleteButton = view.findViewById(R.id.deleteButton);

        }
    }

}