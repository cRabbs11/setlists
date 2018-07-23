package com.example.evgeny.setlist_mobile.model;

/**
 * Created by Evgeny on 23.07.2018.
 */

public class City extends BaseModel {
    public String id;
    public String name;
    public String state;
    public String stateCode;
    public Coords coords;
    public Country country;

    public City() {
        coords = new Coords();
        country = new Country();
    }
}
