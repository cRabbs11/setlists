package com.example.evgeny.setlist_mobile.model;

/**
 * Created by Evgeny on 23.07.2018.
 */

public class Song extends BaseModel {
    public String name;
    public String info;
    public boolean tape;
    public Artist cover;

    public Song() {
        cover = new Artist();
    }
}
