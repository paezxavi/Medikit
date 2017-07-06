package com.example.pixa.medikit.Business;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Pixa on 24.04.2017.
 */

public class Position {
    private String name;
    private double lat;
    private double lng;

    public Position(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return name;
    }
}
