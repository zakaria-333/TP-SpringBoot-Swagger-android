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

public class UpdateFiliereActivity extends AppCompatActivity {
    private TextView id;
    private EditText code;
    private EditText libelle;
    private Button update;

    private RequestQueue requestQueue ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_filiere);
        Intent intent = getIntent();


        id = findViewById(R.id.editid);
        code = (EditText) findViewById(R.id.editcode);
        libelle = (EditText) findViewById(R.id.editlibelle);
        update = (Button) findViewById(R.id.updatebtn);

        requestQueue = Volley.newRequestQueue(this);

        String idi = intent.getStringExtra("id");
        String codei = intent.getStringExtra("code");
        String libellei = intent.getStringExtra("libelle");

        id.setText(idi);
        code.setText(codei);
        libelle.setText(libellei);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFiliere();
            }
        });



    }
    private void updateFiliere() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String ids = id.getText().toString();
        String codes = code.getText().toString();
        String libelles = libelle.getText().toString();
        String updateUrl = "http://192.168.1.103:8080/api/v1/filieres/"+ids;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("code", codes);
            jsonBody.put("libelle" , libelles);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                updateUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Filiere updated successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateFiliereActivity.this, FiliereActivity.class);
                startActivity(intent);
                UpdateFiliereActivity.this.finish();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);

    }

}
