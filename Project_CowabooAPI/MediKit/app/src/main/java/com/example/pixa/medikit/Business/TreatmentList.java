package com.example.pixa.medikit.Business;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joel Marques on 21.04.2017.
 */

public class TreatmentList {
    private Collection<Treatment> treatments = new ArrayList<Treatment>();
    public void addTreatment(Treatment t) {treatments.add(t);}
    public Collection<Treatment> getTreatments() {return treatments;}
}
