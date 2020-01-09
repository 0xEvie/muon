package com.github.zeroxevie.muon.Objects;


import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserCredentials extends RealmObject
{
    @PrimaryKey
    private String id;
    private String password;

    public UserCredentials()
    {
        id = UUID.randomUUID().toString();
        password = "password";
    }

    public UserCredentials(String password)
    {
        id = UUID.randomUUID().toString();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
