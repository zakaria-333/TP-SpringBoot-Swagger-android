package ma.ensa.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ensa.list.beans.Filiere;

public class UpdateEtudiantActivity extends AppCompatActivity {
    private TextView id;
    private EditText username;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private EditText phone;

    private Spinner filiereSpinner;
    private Button update;

    private RequestQueue requestQueue ;

    private Filiere hamburg;
    List<Filiere> filiereList = new ArrayList<>();

    int filierei;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_etudiant);
        Intent intent = getIntent();


        id = findViewById(R.id.editid);
        username = (EditText) findViewById(R.id.editusername);
        password = (EditText) findViewById(R.id.editpassword);
        firstname = (EditText) findViewById(R.id.editfirstname);
        lastname = (EditText) findViewById(R.id.editlastname);
        phone = (EditText) findViewById(R.id.editphone);
        filiereSpinner = (Spinner) findViewById(R.id.editfiliere);
        update = (Button) findViewById(R.id.button);

        requestQueue = Volley.newRequestQueue(this);

        fetchSpinnerOptionsFromAPI();

        String idi = intent.getStringExtra("id");
        String usernamei = intent.getStringExtra("username");
        String passwordi = intent.getStringExtra("password");
        String firstnamei = intent.getStringExtra("firstname");
        String lastnamei = intent.getStringExtra("lastname");
        String phonei = intent.getStringExtra("phone");
        String filiereString = intent.getStringExtra("filiere");
        if (filiereString != null) {
            filierei = Integer.parseInt(filiereString);
        } else {
        }


        id.setText(idi);
        username.setText(usernamei);
        password.setText(passwordi);
        firstname.setText(firstnamei);
        lastname.setText(lastnamei);
        phone.setText(phonei);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEtudiant();
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
    private void updateEtudiant() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String ids = id.getText().toString();
        String usernames = username.getText().toString();
        String passwords = password.getText().toString();
        String firstnames = firstname.getText().toString();
        String lastnames = lastname.getText().toString();
        String phones = phone.getText().toString();
        String updateUrl = "http://192.168.1.103:8080/api/student/"+ids;

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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                updateUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Student updated successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateEtudiantActivity.this, EtudiantActivity.class);
                startActivity(intent);
                UpdateEtudiantActivity.this.finish();
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateEtudiantActivity.this, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        for (Filiere filiere : filiereList) {
                            adapter.add(filiere.getCode());
                        }
                        filiereSpinner.setAdapter(adapter);
                        filiereSpinner.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < filiereList.size(); i++) {
                                    if (filiereList.get(i).getId() == filierei) {
                                        filiereSpinner.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        });


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
