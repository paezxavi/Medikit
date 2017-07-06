package com.example.pixa.medikit.Application;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.example.pixa.medikit.Business.Disease;
import com.example.pixa.medikit.Business.Symptom;
import com.example.pixa.medikit.Business.Treatment;
import com.example.pixa.medikit.Business.Type;
import com.example.pixa.medikit.Business.TypeList;
import com.example.pixa.medikit.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pixa on 22.04.2017.
 */

public class ListDiseases {
    private static final String REF_DISEASE = "Ref Disease";
    private static final String[] FROM = {"icon", "nameDisease", "imgCoeur", "imgCerveau", "imgRespiratoires", "imgMusculaires", "imgDermatologiques", "imgIntestinales", "objet"};
    private static final int[] TO = {R.id.img_icon, R.id.tv_disease, R.id.imgRespiratoires, R.id.imgCoeur, R.id.imgDermatologiques, R.id.imgIntestinales, R.id.imgMusculaires, R.id.imgCerveau};

    private List<HashMap<String, Object>> dataTout;
    private SimpleAdapter adapter;

    public ListDiseases (Context context, Collection<Disease> diseases, int typeID) {
        dataTout = new ArrayList<HashMap<String, Object>>(diseases.size());
        /* Création de la liste des données */
        for (Disease d: diseases) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(FROM[0], R.drawable.symptomes);
            map.put(FROM[1], d.getName());
            int i = 2;
            TypeList types = d.getTypes();
            for (Type type: types.getTypes()) {
                map.put(FROM[i], type.getImage(type.getType()));
                i++;
            }
            map.put(FROM[8], d);
            dataTout.add(map);
        }
        adapter = new SimpleAdapter(context, dataTout, typeID == 0 ? R.layout.one_line : R.layout.one_line_search, FROM, TO);
    } // Constructeur

    public SimpleAdapter getAdapter(){
        return adapter;
    }

    public Disease getDisease(AdapterView<?> parent, int position){
        HashMap<String, Object> hm = (HashMap<String, Object>) parent.getItemAtPosition(position);
        Disease d = (Disease) hm.get("objet");
        return d;
    }
}
