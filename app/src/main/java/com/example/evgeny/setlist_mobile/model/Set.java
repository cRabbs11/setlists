package com.example.evgeny.setlist_mobile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 23.07.2018.
 */

public class Set extends BaseModel {
    public String name;
    public String number;
    public String encore;
    public List<Song> songs;

    public Set() {
        songs = new ArrayList<>();
    }
}
