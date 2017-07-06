package com.example.pixa.medikit.Business;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

import static com.example.pixa.medikit.Data.NameWebService.*;

/**
 * Created by Pixa on 20.04.2017.
 */

public class Disease implements Comparable<Disease>{

    private String name;
    private SymptomList symptoms;
    private TreatmentList treatments;

    private TypeList types;

    public Disease(String name){
        this.name = name;
        symptoms = new SymptomList();
        treatments = new TreatmentList();
        types = new TypeList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymptomList getSymptoms() {
        return symptoms;
    }


    public boolean isDiseaseBind (String symptomSearch) {
        for (Symptom sympt: this.symptoms.getSymptoms()) {
            if (symptomSearch.toLowerCase().equals(sympt.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void setSymptoms(SymptomList symptoms) {this.symptoms = symptoms;}

    public TreatmentList getTreatments() {
        return treatments;
    }

    public void setTreatments(TreatmentList treatments) {
        this.treatments = treatments;
    }

    public TypeList getTypes() {return types;}

    public void setTypes(TypeList types) {this.types = types;}

    public boolean equals(Object obj) {
        return this.name.equals(((Disease)obj).getName());
    }

    @Override
    public int compareTo(Disease d){
        return name.compareTo(d.name);
    }


}
