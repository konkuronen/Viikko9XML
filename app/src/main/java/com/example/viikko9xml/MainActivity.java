package com.example.viikko9xml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button nappi;
    private Spinner valikko;
    private TextInputEditText paiva;
    private TextInputEditText alku, loppu;
    private TextInputEditText nimi;
    private ScrollView sv;
    private LinearLayout ll;
    private ArrayAdapter teatteriAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nappi = findViewById(R.id.button);
        valikko = findViewById(R.id.teatterit);
        paiva = findViewById(R.id.paiva);
        alku = findViewById(R.id.alku);
        loppu = findViewById(R.id.loppu);
        nimi = findViewById(R.id.nimi);
        sv = findViewById(R.id.sv);
        ll = findViewById(R.id.ll);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }


    public void readXML (View v) {
        TeatteriLista tLista = TeatteriLista.getInstance();
        tLista.lueXML(tLista);

/*        for (int i = 0 ; i < tLista.getLukumaara() ; i++) {
            tLista.tulostaTeatteri(i);
        }
*/
        final List<String> teatterit = new ArrayList<>();
        for (int i = 0 ; i < tLista.getLukumaara() ; i++) {
            System.out.println(tLista.getID(i));
            System.out.println(tLista.getName(i));
            teatterit.add(tLista.getName(i));
        }

        ArrayAdapter teatteriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teatterit);
        valikko.setAdapter(teatteriAdapter);


    }
    public void nollaa(View v) {
        nimi.setText(null);
        alku.setText(null);
        loppu.setText(null);
        paiva.setText(null);
        ll.removeAllViews();
    }

    public void naytokset(View v) {
        ll.removeAllViews();
        TeatteriLista tLista = TeatteriLista.getInstance();
        String valinta = valikko.getSelectedItem().toString();
        String nimis = nimi.getText().toString();
        int ID = 0;
        for (int i = 0 ; i < tLista.getLukumaara() ; i++) {
            if (valinta == tLista.getName(i)) {
                ID = tLista.getID(i);
            }
        }
        String pvm = paiva.getText().toString();
        if (pvm == null) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");

            pvm = format.format(date);
        }
        String alkuaika = alku.getText().toString();
        String loppuaika = loppu.getText().toString();
        if (alku.getText().toString().isEmpty()) {
            alkuaika = "00:00";
        }

        if (loppu.getText().toString().isEmpty()) {
            loppuaika = "23:59";
        }
        if (nimis.matches("")) {

            NodeList nList = tLista.parsiNaytokset(ID, pvm);
            for (int i = 0 ; i < nList.getLength(); i++) {
                TextView tw = new TextView(this);
                if (tLista.haeAika(nList, i, alkuaika, loppuaika) == null) {
                    continue;
                }
                tw.setText("Teatteri: " + tLista.haeTeatteri(nList, i) + "\nNimi: " + tLista.haeNaytos(nList, i) + "\nklo: " +  tLista.haeAika(nList, i, alkuaika, loppuaika) + "\n" + tLista.haeSali(nList, i));
                ll.addView(tw);
            }
        } else {
            TextView tw = new TextView(this);
            tw.setText("Nimi: " + nimi.getText().toString());
            ll.addView(tw);
            if (ID == 1029) {
                List<String> lista = Arrays.asList("1014","1015","1016","1017","1041","1018","1019","1021","1022");
                for (int a = 0; a < lista.size(); a++) {
                    ID = Integer.parseInt(lista.get(a));
                    NodeList nList = tLista.parsiNaytokset(ID, pvm);
                    for (int i = 0 ; i < nList.getLength(); i++) {
                        tw = new TextView(this);
                        if (tLista.haeAika(nList, i, alkuaika, loppuaika) == null) {
                            continue;
                        }
                        if (tLista.haeNaytos(nList, i).equals(nimis)) {
                            tw.setText("Teatteri: " + tLista.haeTeatteri(nList, i) + "\nklo: " + tLista.haeAika(nList, i, alkuaika, loppuaika) + "\n" + tLista.haeSali(nList, i));
                            ll.addView(tw);
                        }
                    }
                }

            } else {
                NodeList nList = tLista.parsiNaytokset(ID, pvm);
                for (int i = 0 ; i < nList.getLength(); i++) {
                    tw = new TextView(this);
                    if (tLista.haeAika(nList, i, alkuaika, loppuaika) == null) {
                        continue;
                    }
                    if (tLista.haeNaytos(nList, i).equals(nimis)) {
                        tw.setText("Teatteri: " + tLista.haeTeatteri(nList, i) + "\nklo: " + tLista.haeAika(nList, i, alkuaika, loppuaika) + "\n" + tLista.haeSali(nList, i));
                        ll.addView(tw);
                    }
                }
            }


        }





        /*        for (int i = 0 ; i < tLista.getLukumaara() ; i++) {
            TextView tw = new TextView(this);
            tw.setText(tLista.getName(i));
            ll.addView(tw);
        }
*/


    }
}

