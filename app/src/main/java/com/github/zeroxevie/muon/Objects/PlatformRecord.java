package com.github.zeroxevie.muon.Objects;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlatformRecord extends RealmObject
{
    @PrimaryKey
    private String id;

    private String platform_title;
    private String platform_type;
    private Boolean favourite;


    public PlatformRecord(String id, String platform_title, String platform_type)
    {
        this.id = UUID.randomUUID().toString();
        this.platform_title = platform_title;
        this.platform_type = platform_type;
        this.favourite = false;
    }

    public PlatformRecord()
    {
        platform_title = "default.com";
        platform_type = "Website";
        id = UUID.randomUUID().toString();
        favourite = false;
    }

    public String toString()
    {
        return platform_title;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPlatform_type()
    {
        return platform_type;
    }

    public void setPlatform_type(String platform_type)
    {
        this.platform_type = platform_type;
    }

    public String getPlatform_title()
    {
        return platform_title;
    }

    public void setPlatform_title(String platform_title)
    {
        this.platform_title = platform_title;
    }

    public Boolean isFavourite()
    {
        return favourite;
    }

    public void setFavourite(Boolean favourite)
    {
        this.favourite = favourite;
    }
}
