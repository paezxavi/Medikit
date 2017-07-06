package com.example.pixa.medikit.Business;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.pixa.medikit.Data.NameWebService.*;

/**
 * Created by Pixa on 20.04.2017.
 */

public class Symptom {

    private String name;

    public Symptom(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
