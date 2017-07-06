package com.example.pixa.medikit.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.RecipientEntry;
import com.example.pixa.medikit.Application.ListDiseases;
import com.example.pixa.medikit.Application.TimeLimitedCodeBlock;
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
import com.example.pixa.medikit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.datatype.Duration;

import static com.example.pixa.medikit.Data.NameWebService.DICTIONARY;
import static com.example.pixa.medikit.Data.NameWebService.ENTRIES;
import static com.example.pixa.medikit.Data.NameWebService.GETSYMPTOMS;
import static com.example.pixa.medikit.Data.NameWebService.ID;
import static com.example.pixa.medikit.Data.NameWebService.SYMPTOMS;
import static com.example.pixa.medikit.Data.NameWebService.TAGS;
import static com.example.pixa.medikit.Data.NameWebService.TREATMENTS;
import static com.example.pixa.medikit.Data.NameWebService.TYPE;
import static com.example.pixa.medikit.Data.NameWebService.VALUE;

/**
 * Created by Joel Marques on 20.04.2017.
 */

public class SearchActivity extends AppCompatActivity {

    private RecipientEditTextView myRetv;
    private Button btnSearch;
    private ProgressBar pgb;
    private Collection<Disease> liste;
    private DiseaseList res;
    private ListView lst;
    private ListDiseases lstDis;
    private Data data;
    private TextView tvSuggestions;
    private TextView tvSensation;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView imgNoFound;

    private static final int DISEASE_SELECTED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        liste = MainActivity.diseases.getDiseases();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_diseases);
        lst = (ListView)findViewById(R.id.lv_res_search);
        lst.setVisibility(View.GONE);
        tvSensation = (TextView) findViewById(R.id.tvSensation);

        img1 = (ImageView) findViewById(R.id.img1S);
        img2 = (ImageView) findViewById(R.id.img2S);
        img3 = (ImageView) findViewById(R.id.img3S);
        img4 = (ImageView) findViewById(R.id.img4S);
        img5 = (ImageView) findViewById(R.id.img5S);
        img6 = (ImageView) findViewById(R.id.img6S);
        imgNoFound = (ImageView) findViewById(R.id.imgNoFound);
        tvSuggestions = (TextView) findViewById(R.id.tvSuggestions);
        tvSuggestions.setVisibility(View.GONE);
        data = Data.getInstance();
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Disease d = lstDis.getDisease(adapterView,i);
                data.setActualDisease(d);
                Intent intent = new Intent(getApplicationContext(), DiseaseSelectedActivity.class);
                startActivityForResult(intent, DISEASE_SELECTED);
            }
        });

        myRetv = (RecipientEditTextView) findViewById(R.id.example_retv);

        myRetv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        myRetv.setAdapter(new BaseRecipientAdapter(this));
        btnSearch = (Button) findViewById(R.id.btnSearch);
        pgb = (ProgressBar)findViewById(R.id.progressBar);
        pgb.setVisibility(View.GONE);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setEnabled(false);
                res = new DiseaseList();
                res.clear();
                imgNoFound.setVisibility(View.GONE);
                lstDis = new ListDiseases(getApplicationContext(), res.getDiseases(), 1);
                lst.setAdapter(lstDis.getAdapter());
                pgb.setVisibility(View.VISIBLE);
                List<RecipientEntry> list = myRetv.getAllRecipients();
                int cpt = 0;
                for (RecipientEntry el: list) {
                    // Lancer la requête de recherche
                    for (Disease d : liste) {
                        if (d.isDiseaseBind(el.getDisplayName()) && !res.getDiseases().contains(d)) {
                            res.addDisease(d);
                            cpt++;
                        }
                    }
                }


                TimerTask task;

                final int finalCpt = cpt;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pgb.setVisibility(View.INVISIBLE);
                                tvSuggestions.setVisibility(View.VISIBLE);
                                lst.setVisibility(View.VISIBLE);
                                lstDis = new ListDiseases(getApplicationContext(), res.getDiseases(), 1);
                                lst.setAdapter(lstDis.getAdapter());
                                btnSearch.setEnabled(true);
                                imgNoFound.setVisibility(finalCpt == 0? View.VISIBLE : View.GONE);
                            }
                        });
                        cancel();
                    }
                };

                Timer timer = new Timer();
                timer.schedule(task, 1000, 2000);



            }
        });
    }





}
