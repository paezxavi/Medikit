package com.example.pixa.medikit.Business;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Virus on 12.05.17.
 */

public class Data {
    private static Data instance = new Data();
    private Disease disease;

    private Collection<Disease> listeDisease = null; /* La liste de tous les cadeaux */

    private Data () {}

    public static Data getInstance () {return instance;}

    public Collection<Disease> getListeDisease () {
        return listeDisease;
    } // getListeCadeaux*/

    public void setListeDiseases(Collection<Disease> liste){
        listeDisease = liste;
    }

    public void setActualDisease(Disease d){
        this.disease = d;
    }

    public Disease getActualDisease(){
        return disease;
    }

}
