package com.example.evgeny.setlist_mobile.model;

import java.util.ArrayList;
import java.util.List;

public class Setlist extends BaseModel {

    public Artist artist;
    public Venue venue;
    public Tour tour;
    public Set set;
    public String info;
    public String url;
    public String id;
    public String versionId;
    public String lastFmEventId;
    public String eventDate;
    public String lastUpdated;
    public List<Set> sets;

    public Setlist() {
        artist = new Artist();
        venue = new Venue();
        tour = new Tour();
        set = new Set();
        sets = new ArrayList<>();
    }
}
