package com.example.pixa.medikit.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SearchView;

import com.example.pixa.medikit.Application.ListDiseases;
import com.example.pixa.medikit.Business.Data;
import com.example.pixa.medikit.Business.Disease;
import com.example.pixa.medikit.Business.Symptom;
import com.example.pixa.medikit.Business.Treatment;
import com.example.pixa.medikit.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Joel Marques on 21.04.2017.
 */

public class DataBaseActivity extends AppCompatActivity {

    private Collection<Disease> allDiseases;
    Collection<Disease> filtre;
    private ListView lvDiseases;
    private SearchView svDisease;
    private ListDiseases lstDiseases;
    private Data data;
    private static final int DISEASE_SELECTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        allDiseases = MainActivity.diseases.getDiseases();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        data = Data.getInstance();
        defineVariables();
        creationAdapter();
        definirListener();
    }

    private void definirListener(){
        lvDiseases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj = adapterView.getItemAtPosition(i);
                data.setActualDisease(lstDiseases.getDisease(adapterView,i));
                Intent intent = new Intent(getApplicationContext(), DiseaseSelectedActivity.class);
                startActivityForResult(intent, DISEASE_SELECTED);
            }
        });
        svDisease.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtre = new ArrayList<>();
                if(newText == "" || newText == null){
                    filtre = allDiseases;
                } else {
                    for(Disease d : allDiseases){
                        String nom = d.getName();
                        if(nom.trim().toLowerCase().startsWith(newText.trim().toLowerCase())){
                            filtre.add(d);
                        }
                    }

                }
                lstDiseases = new ListDiseases(getApplicationContext(), filtre, 0);
                lvDiseases.setAdapter(lstDiseases.getAdapter());
                return false;
            }// C'est pas beau, mais j'arrive plus à faire les filtres correctement
        });

    }

    private void defineVariables() {
        lvDiseases = (ListView) findViewById(R.id.lv_disease);
        svDisease = (SearchView) findViewById(R.id.sv_disease);
    }

    private void creationAdapter() {
        lstDiseases = new ListDiseases(getApplicationContext(), allDiseases, 0);
        lvDiseases.setAdapter(lstDiseases.getAdapter());
    }

}
