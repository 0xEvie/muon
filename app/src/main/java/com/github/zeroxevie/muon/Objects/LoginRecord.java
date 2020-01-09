package com.github.zeroxevie.muon.Objects;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LoginRecord extends RealmObject
{
    @PrimaryKey
    private String id;

    private String username;
    private String password;
    private String platform_title;
    private String platform_type;
    private Boolean favourite = false;

    public LoginRecord(String username, String password, String platform_title, String platform_type)
    {
        this.username = username;
        this.password = password;
        this.platform_title = platform_title;
        this.platform_type = platform_type;
        id = UUID.randomUUID().toString();
    }

    public LoginRecord()
    {
        username = "Default";
        password = "Default";
        platform_title = "default platform";
        platform_type = "Website";
        id = UUID.randomUUID().toString();
    }

    //Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString()
    {
        return username;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPlatform_title()
    {
        return platform_title;
    }

    public void setPlatform_title(String platform_title)
    {
        this.platform_title = platform_title;
    }

    public String getPlatform_type()
    {
        return platform_type;
    }

    public void setPlatform_type(String platform_type)
    {
        this.platform_type = platform_type;
    }

    public Boolean getFavourite()
    {
        return favourite;
    }

    public void setFavourite(Boolean favourite)
    {
        this.favourite = favourite;
    }
}
