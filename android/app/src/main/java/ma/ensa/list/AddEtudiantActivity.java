package ma.ensa.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensa.list.adapter.EtudiantAdapter;
import ma.ensa.list.beans.Etudiant;
import ma.ensa.list.beans.Filiere;

public class AddEtudiantActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private EditText phone;

    private Spinner filiereSpinner;
    private Button add;

    private RequestQueue requestQueue;

    private Filiere hamburg;

    List<Filiere> filiereList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        username = findViewById(R.id.addusername);
        password = findViewById(R.id.addpassword);
        firstname = findViewById(R.id.addfirstname);
        lastname = findViewById(R.id.addlastname);
        phone = findViewById(R.id.addphone);
        filiereSpinner = findViewById(R.id.addfiliere);
        add = findViewById(R.id.button);
        requestQueue = Volley.newRequestQueue(this);

        fetchSpinnerOptionsFromAPI();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEtudiant();
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

    private void addEtudiant() {
        String usernames = username.getText().toString();
        String passwords = password.getText().toString();
        String firstnames = firstname.getText().toString();
        String lastnames = lastname.getText().toString();
        String phones = phone.getText().toString();
        String filiereId = String.valueOf(hamburg.getId());


        String insertUrl = "http://192.168.1.103:8080/api/student";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", usernames);
            jsonBody.put("password", passwords);
            jsonBody.put("firstName", firstnames);
            jsonBody.put("lastName", lastnames);
            jsonBody.put("phone", phones);
            JSONObject filiereObject = new JSONObject();
            filiereObject.put("id", hamburg.getId());
            jsonBody.put("filiere", filiereObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Student created successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddEtudiantActivity.this, EtudiantActivity.class);
                startActivity(intent);
                AddEtudiantActivity.this.finish();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEtudiantActivity.this, android.R.layout.simple_spinner_item);
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

