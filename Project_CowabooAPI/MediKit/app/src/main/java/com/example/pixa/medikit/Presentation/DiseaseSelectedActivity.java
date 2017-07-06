package com.example.pixa.medikit.Presentation;

import android.os.Bundle;

import com.example.pixa.medikit.Business.Data;
import com.example.pixa.medikit.Business.Symptom;
import com.example.pixa.medikit.Business.SymptomList;
import com.example.pixa.medikit.Business.Treatment;
import com.example.pixa.medikit.Business.TreatmentList;
import com.example.pixa.medikit.Business.Type;
import com.example.pixa.medikit.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collection;

/**
 * Created by Virus on 12.05.17.
 */

public class DiseaseSelectedActivity extends AppCompatActivity{

    private Data data;

    /*Variables*/
    private TextView tvTitle, tvSymptoms, tvSymptomsDet, tvTreatmentsDet, tvTreatments, tvSatisfaction;
    private Button btnNo, btnYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);
        data = Data.getInstance();
        defineVariables();
        showInfo();
        //Si vous trouvez que les informations vous ont aidées, alors ca met des points stellar
    }

    private void defineVariables(){
        tvTitle = (TextView)findViewById(R.id.title);
        tvSymptoms = (TextView)findViewById(R.id.symptoms);
        tvTreatments = (TextView)findViewById(R.id.treatments);
        tvSymptomsDet = (TextView)findViewById(R.id.symptDet);
        tvTreatmentsDet = (TextView)findViewById(R.id.treatDet);
        tvSatisfaction = (TextView)findViewById(R.id.tv_satisf);
        btnNo = (Button)findViewById(R.id.btnNo);
        btnYes = (Button)findViewById(R.id.btnYes);
    }

    private void showInfo(){
        tvTitle.setText(data.getActualDisease().getName());
        tvSymptoms.setText(getString(R.string.symptoms));
        showSymptoms();
        showTreatments();
        tvSymptoms.setText("Symptômes");
        tvTreatments.setText("Traitements");
        tvSatisfaction.setText("Are you satisfied with your research?");

        int i = 0;
        for (Type type : data.getActualDisease().getTypes().getTypes()) {
            int idImage = 0;
            switch (i) {
                case 0:
                    idImage =  R.id.imgDetails0;

                    break;
                case 1:
                    idImage =  R.id.imgDetails1;
                    break;
                case 2:
                    idImage =  R.id.imgDetails2;
                    break;
                case 3:
                    idImage =  R.id.imgDetails3;
                    break;
                case 4:
                    idImage =  R.id.imgDetails4;
                    break;
                case 5:
                    idImage =  R.id.imgDetails5;
                    break;
            }
            i++;
            ImageView img = (ImageView) findViewById(idImage);
            img.setVisibility(View.VISIBLE);
            img.setImageDrawable(getDrawable(type.getImage(type.getType())));
        }
    }

    private void showSymptoms(){
        StringBuilder str = new StringBuilder();

        SymptomList symptoms = data.getActualDisease().getSymptoms();
        for (Symptom symp: symptoms.getSymptoms()) {
            str.append(" - " + symp.getName() + "\n");
        }
        tvSymptomsDet.setText(str.toString());
    }

    private void showTreatments(){

        StringBuilder str = new StringBuilder();

        TreatmentList treatments = data.getActualDisease().getTreatments();
        for (Treatment treat: treatments.getTreatments()) {
            str.append(" - " + treat.getName() + "\n");
        }
        tvTreatmentsDet.setText(str.toString());
    }
}
