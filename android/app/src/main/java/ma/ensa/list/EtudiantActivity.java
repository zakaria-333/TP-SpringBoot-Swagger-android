package ma.ensa.list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import ma.ensa.list.beans.Etudiant;

public class EtudiantActivity extends AppCompatActivity {

    String insertUrl = "http://192.168.1.103:8080/api/student";
    private List<Etudiant> etudiants = new ArrayList<>();
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);

        btn = findViewById(R.id.add);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EtudiantActivity.this, AddEtudiantActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycle_view);
        requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, insertUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rep", response);
                        Type type = new TypeToken<List<Etudiant>>() {
                        }.getType();
                        etudiants = new Gson().fromJson(response, type);
                        Log.d("Etudiants", "Nombre d'étudiants récupérés : " + etudiants.size());

                        for (Etudiant e : etudiants) {
                            Log.d("b", e.toString());
                        }
                        etudiantAdapter = new EtudiantAdapter(EtudiantActivity.this, etudiants);
                        recyclerView.setAdapter(etudiantAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(EtudiantActivity.this));
                        etudiantAdapter.notifyDataSetChanged();


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