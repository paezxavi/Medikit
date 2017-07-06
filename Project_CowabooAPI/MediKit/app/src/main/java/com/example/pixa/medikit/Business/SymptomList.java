package com.example.pixa.medikit.Business;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joel Marques on 21.04.2017.
 */

public class SymptomList {
    private Collection<Symptom> symptoms = new ArrayList<Symptom>();
    public void addSymptom(Symptom s) {symptoms.add(s);}
    public Collection<Symptom> getSymptoms() {return symptoms;}
}
