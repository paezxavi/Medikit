package com.example.pixa.medikit.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.pixa.medikit.Data.NameWebService.*;

import com.example.pixa.medikit.Business.Data;
import com.example.pixa.medikit.Business.Disease;
import com.example.pixa.medikit.Business.DiseaseList;
import com.example.pixa.medikit.Business.Symptom;
import com.example.pixa.medikit.Business.SymptomList;
import com.example.pixa.medikit.Business.Treatment;
import com.example.pixa.medikit.Business.TreatmentList;
import com.example.pixa.medikit.Business.Type;
import com.example.pixa.medikit.Business.TypeList;
import com.example.pixa.medikit.Data.GetFromUrl;
import com.example.pixa.medikit.Data.GetFromUrl.Listener;
import com.example.pixa.medikit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Listener {

    public static DiseaseList diseases = new DiseaseList();
    private Data data;


    public class AllDiseases{
        public AllDiseases(JSONObject json, MainActivity main) throws JSONException{
            System.out.println(json);
            JSONObject list = json.getJSONObject("tag_list");
            JSONObject dis = list.getJSONObject("list");
            Iterator<String> iterator = dis.keys();

            while (iterator.hasNext()) {
                diseases.addDisease(new Disease(iterator.next()));
            }

            for (Disease d: diseases.getDiseases()) {
                new GetFromUrl(main, 2).execute(GETSYMPTOMS + d.getName());
            }
        }
    }

    public class AddTreatmentsToDiseases{
        public AddTreatmentsToDiseases(JSONObject json) throws JSONException{
            JSONObject dict = json.getJSONObject(DICTIONARY);
            String name = dict.getString(ID);
            Disease d = diseases.getDisease(name);
            if (d != null) {
                JSONObject entries = dict.getJSONObject(ENTRIES);
                Iterator<String> iterator = entries.keys();
                SymptomList symptoms = new SymptomList();
                TreatmentList treatments = new TreatmentList();
                TypeList types = new TypeList();
                while (iterator.hasNext()) {
                    String idEntry = iterator.next();
                    JSONObject entry = entries.getJSONObject(idEntry);
                    if (entry.getString(TAGS).equals(SYMPTOMS)) {
                        String[] sympTab = entry.getString(VALUE).split("\\|");
                        for (String nom : sympTab) {
                            symptoms.addSymptom(new Symptom(nom));
                        }
                    } else if (entry.getString(TAGS).equals(TREATMENTS)) {
                        String[] treatsTab = entry.getString(VALUE).split("\\|");
                        for (String nom : treatsTab) {
                            treatments.addTreatment(new Treatment(nom));
                        }
                    } else if (entry.getString(TAGS).equals(TYPE)) {
                        String[] typeTab = entry.getString(VALUE).split("\\|");
                        System.out.println(entry.getString(VALUE));
                        for (String nom : typeTab) {
                            System.out.println("Type : " + nom);
                            types.addType(new Type(nom));
                        }

                    }
                }

                d.setSymptoms(symptoms);
                d.setTreatments(treatments);
                d.setTypes(types);
            }
        }
    }

    private void getInfo(){
        new GetFromUrl(this, 1).execute(GETDISEASES);
    }
    @Override
    public void onGetFromUrlResult (JSONObject json) {
        try{
            int type = json.getInt("type");
            if (type == 1) {
                new AllDiseases(json, this);
            } else if (type == 2) {
                new AddTreatmentsToDiseases(json);
            } else {

            }
        } catch (JSONException e) { System.out.println("Erreur"); return;}
    } // onGetFromUrlResult

    @Override
    /* Listener de la tâche GetFromUrl: il y a eu une erreur */
    public void onGetFromUrlError (Exception e) {
        System.out.println("Erreur onGetFromUrlError " + e);
    } // onGetFromUrlError

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getInfo();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_database) {
            Intent intent = new Intent(getApplicationContext(),DataBaseActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_geolocation) {
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share_exp) {
            Intent intent = new Intent(getApplicationContext(), LoginScreenActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
