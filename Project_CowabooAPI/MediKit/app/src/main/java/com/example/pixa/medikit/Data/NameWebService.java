package com.example.pixa.medikit.Data;

/**
 * Created by Pixa on 20.04.2017.
 */

public class NameWebService {
    public static final String KEY = "SA55VCUHXIYBXRY4WUC22OOOKPAXMJGKTUA743G44B5AX5CTTMGOYALG"; // Secret Key Cowaboo
    public static final String URL_BASE = "http://groups.cowaboo.net/group11/public/api/";

    /* GetTags */
    public static final String OBSERVATORY = "observatory";
    public static final String OBSERVATORYID = "observatoryId";

    public static final String DICTIONARY = "dictionary";
    public static final String ID = "id";
    public static final String ENTRIES = "entries";
    public static final String TAGS = "tags";
    public static final String VALUE = "value";

    /* Commandes */
    public static final String GETDISEASES = URL_BASE + "tags"; /* Récupération de toutes les informations */
    public static final String GETSYMPTOMS = URL_BASE + OBSERVATORY + "?" + OBSERVATORYID + "=";

    public static final String SYMPTOMS = "||Symptômes||";
    public static final String TREATMENTS = "||Traitements||";
    public static final String TYPE = "||Type||";

}
