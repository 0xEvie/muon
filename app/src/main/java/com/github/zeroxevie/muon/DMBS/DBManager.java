package com.github.zeroxevie.muon.DMBS;

import android.content.Context;
import android.util.Log;

import java.util.Objects;
import java.util.UUID;

import com.github.zeroxevie.muon.Objects.LoginRecord;
import com.github.zeroxevie.muon.Objects.PlatformRecord;
import com.github.zeroxevie.muon.Objects.UserCredentials;
import io.realm.Realm;
import io.realm.RealmResults;

public class DBManager
{
    Realm realm;

    public DBManager(Context initContext)
    {
        Realm.init(initContext);
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealm()
    {
        return realm;
    }

    public boolean userExists()
    {
        UserCredentials credentialsToFind = realm.where(UserCredentials.class).findFirst();

        return credentialsToFind != null;
    }

    public void createUserCredentials(String password)
    {
        realm.beginTransaction();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setPassword(password);
        realm.copyToRealmOrUpdate(userCredentials);
        realm.commitTransaction();
    }

    public void setFavourite(PlatformRecord platformRecord)
    {
        realm.beginTransaction();
        platformRecord.setFavourite(!platformRecord.isFavourite());
        realm.commitTransaction();
    }

    public void addLogin(String platform_title, String platform_type, String username, String password)
    {
        try
        {
            //Try to find a login record with these details
            LoginRecord loginRecordToFind = realm.where(LoginRecord.class).equalTo
                    ("username", username).and().equalTo("platform_title", platform_title).findFirst();

            realm.beginTransaction(); //Begin Transaction

            //If record doesn't exist
            if (loginRecordToFind == null)
            {
                //Create record
                LoginRecord loginRecordToAdd = new LoginRecord();
                loginRecordToAdd.setId(UUID.randomUUID().toString());
                loginRecordToAdd.setPlatform_title(platform_title);
                loginRecordToAdd.setPlatform_type(platform_type);
                loginRecordToAdd.setUsername(username);
                loginRecordToAdd.setPassword(password);
                realm.copyToRealmOrUpdate(loginRecordToAdd);

                //Find Platform record
                PlatformRecord platformRecordToFind = realm.where(PlatformRecord.class).equalTo
                        ("platform_title", platform_title).findFirst();

                //If Platform Record doesn't exist
                if (platformRecordToFind == null)
                {
                    PlatformRecord platformRecordToAdd = new PlatformRecord();
                    platformRecordToAdd.setId(UUID.randomUUID().toString());
                    platformRecordToAdd.setPlatform_title(platform_title);
                    platformRecordToAdd.setPlatform_type(platform_type);
                    realm.copyToRealmOrUpdate(platformRecordToAdd);
                }
            }

            Log.v("Password_Manager", "Addition Successful");
            realm.commitTransaction(); //Commit Transaction
        } catch (Exception e)
        {
            Log.e("Password_Manager", e.getMessage());
        }
    }


    public void deleteLogin(String id)
    {
        try
        {

            //Find record that corresponds with ID
            LoginRecord loginRecordToDelete = realm.where(LoginRecord.class).equalTo
                    ("id", id).findFirst();

            String platform_title = Objects.requireNonNull(loginRecordToDelete).getPlatform_title();

            realm.beginTransaction();
            loginRecordToDelete.deleteFromRealm();
            realm.commitTransaction();

            //When deleting login record, delete all website records if no logins correspond
            deleteWebsiteIfLoginListEmpty(platform_title);

            Log.v("Password_Manager", "Delete Successful");
        } catch (Exception e)
        {
            Log.e("Password_Manager", e.getMessage());
        }
    }

    public void deleteWebsiteIfLoginListEmpty(final String platform_title)
    {
        try
        {
            PlatformRecord websiteRecordToDelete = realm.where(PlatformRecord.class).equalTo
                    ("platform_title", platform_title).findFirst();

            RealmResults<LoginRecord> loginRecords = realm.where(LoginRecord.class).equalTo("platform_title", platform_title).findAll();

            realm.beginTransaction();
            if (websiteRecordToDelete != null)
            {
                if (loginRecords.isEmpty())
                {
                    websiteRecordToDelete.deleteFromRealm();
                    Log.v("Password_Manager", "Platform deleted from realm");
                } else
                {
                    Log.v("Password_Manager", "Record list not empty, not deletion of website " +
                            "record needed");
                }
            }
            realm.commitTransaction();
            Log.v("Password_Manager", "Transaction successful");
        } catch (Exception e)
        {
            Log.e("Password_Manager", e.getMessage());
        }
    }

    public boolean loginListIsEmpty(final String platform_title)
    {
        boolean isEmpty = true;

        PlatformRecord platformRecord = realm.where(PlatformRecord.class).equalTo("platform_title",
                platform_title).findFirst();

        RealmResults<LoginRecord> loginRecords = realm.where(LoginRecord.class).equalTo("platform_title", platform_title).findAll();


        if (platformRecord != null && !loginRecords.isEmpty())
        {
            isEmpty = false;
        }

        return isEmpty;

    }

    public void deletePlatformRecord(PlatformRecord recordToDelete)
    {
        try
        {

            if (recordToDelete != null)
            {
                realm.beginTransaction();
                recordToDelete.deleteFromRealm();
                realm.commitTransaction();
            }

            Log.v("Password_Manager", "Delete Successful");
        } catch (Exception e)
        {
            Log.e("Password_Manager", e.getMessage());
        }
    }

    public void deleteAll()
    {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    public void editRecord(String id, String newUsername, String newPassword)
    {
        try
        {
            LoginRecord loginRecordToEdit = realm.where(LoginRecord.class).equalTo("id", id).findFirst();

            realm.beginTransaction();
            Objects.requireNonNull(loginRecordToEdit).setUsername(newUsername);
            loginRecordToEdit.setPassword(newPassword);
            realm.copyToRealmOrUpdate(loginRecordToEdit);
            realm.commitTransaction();
        } catch (Exception e)
        {
            Log.e("Password_Manager", e.getMessage());
        }
    }

    public boolean validateLoginRecord(String username, String password)
    {
        return ((username != null) && username.matches("[A-Za-z0-9_@.]+")) &&
                ((password != null) && password.matches("[A-Za-z0-9_:/=&?#{};><+|!@£$%^*().,]+"));
    }

    public boolean validatePassword(String password)
    {
        return ((password != null) && password.matches("[A-Za-z0-9_:/=&?#{};><+|!@£$%^*().,]+"));
    }


    public boolean validateWebsite(String website)
    {
        return ((website != null) && website.matches("[A-Za-z0-9:/=&?.#_]+"));
    }
}

