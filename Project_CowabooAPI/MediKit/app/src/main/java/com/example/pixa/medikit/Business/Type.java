package com.example.pixa.medikit.Business;

import com.example.pixa.medikit.R;

/**
 * Created by Joel Marques on 17.05.2017.
 */

public class Type {
    private String nom;
    public Type (String nom) {
        this.nom = nom;
    }

    public String getType () {
        return this.nom;
    }

    public void setType (String nom) {
        this.nom = nom;
    }

    public String toString() {
        return this.nom;
    }


    public int getImage(String nom) {
        int image = 0;
        switch (nom) {
            case "Dermatologique":  image = R.drawable.type_dermatologique;
                break;
            case "Coeur":  image = R.drawable.type_coeur;
                break;
            case "Cerveau":  image = R.drawable.type_cerveau;
                break;
            case "Respiratoire":  image = R.drawable.type_respiratoire;
                break;
            case "Musculaire":  image = R.drawable.type_musculaires;
                break;
            case "Intestinale":  image = R.drawable.type_intestinales;
                break;
        }
        return image;
    }


}
