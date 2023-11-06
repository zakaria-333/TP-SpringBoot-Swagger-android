package ma.ensa.list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ma.ensa.list.adapter.EtudiantAdapter;
import ma.ensa.list.adapter.FAdapter;
import ma.ensa.list.beans.Etudiant;
import ma.ensa.list.beans.Filiere;

public class f extends AppCompatActivity {

    String insertUrl = "http://192.168.1.103:8080/api/student/filiere/";
    private List<Etudiant> etudiants = new ArrayList<>();
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;

    private Spinner filiereSpinner;
    private FAdapter fAdapter;

    private Button btn;

    List<Filiere> filiereList = new ArrayList<>();

    private Filiere hamburg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f);
        requestQueue = Volley.newRequestQueue(this);

        fetchSpinnerOptionsFromAPI();

        btn = findViewById(R.id.search);

        filiereSpinner = findViewById(R.id.spinner);

        recyclerView = findViewById(R.id.etudiantslist);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, insertUrl+hamburg.getId(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("rep", response);
                                Type type = new TypeToken<List<Etudiant>>() {
                                }.getType();
                                etudiants = new Gson().fromJson(response, type);
                                fAdapter = new FAdapter(f.this, etudiants);
                                recyclerView.setAdapter(fAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(f.this));
                                fAdapter.notifyDataSetChanged();


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("err", error.toString());
                    }
                });

                requestQueue.add(stringRequest);

            }
        });

        filiereSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Filiere selectedFiliere = filiereList.get(position);
                Log.d("FiliereSelected", "ID: " + selectedFiliere.getId() + " Code: " + selectedFiliere.getCode() + " Libelle: " + selectedFiliere.getLibelle());
                hamburg = new Filiere(selectedFiliere.getId(), selectedFiliere.getCode(), selectedFiliere.getLibelle());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchSpinnerOptionsFromAPI() {
        String Url = "http://192.168.1.103:8080/api/v1/filieres";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REPONSE", response);
                        Type type = new TypeToken<List<Filiere>>() {
                        }.getType();
                        filiereList = new Gson().fromJson(response, type);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(f.this, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        for (Filiere filiere : filiereList) {
                            adapter.add(filiere.getCode());
                        }
                        filiereSpinner.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err", error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }
}