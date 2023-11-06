package ma.ensa.list.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


import ma.ensa.list.EtudiantActivity;
import ma.ensa.list.R;
import ma.ensa.list.UpdateEtudiantActivity;
import ma.ensa.list.beans.Etudiant;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {
    private static final String TAG = "EtudiantAdapter";
    private List<Etudiant> etudiants;
    private Context context;

    private RequestQueue requestQueue ;


    public EtudiantAdapter(Context context, List<Etudiant> etudiants) {
        this.etudiants = etudiants;
        this.context = context;

    }


    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.etudiant_item,
                viewGroup, false);
        final EtudiantViewHolder holder = new EtudiantViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder starViewHolder, int i) {

        Log.d(TAG, "onBindView call ! "+ i);
        starViewHolder.username.setText(etudiants.get(i).getUsername());
        starViewHolder.password.setText(etudiants.get(i).getPassword());
        starViewHolder.firstname.setText(etudiants.get(i).getFirstName());
        starViewHolder.lastname.setText(etudiants.get(i).getLastName());
        starViewHolder.phone.setText(etudiants.get(i).getPhone());
        starViewHolder.filiere.setText(etudiants.get(i).getFiliere().getCode());
        starViewHolder.id.setText(etudiants.get(i).getId()+"");
        starViewHolder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateEtudiantActivity.class);
                intent.putExtra("id",starViewHolder.id.getText().toString());
                intent.putExtra("username",etudiants.get(i).getUsername());
                intent.putExtra("password",etudiants.get(i).getPassword());
                intent.putExtra("firstname",etudiants.get(i).getFirstName());
                intent.putExtra("lastname",etudiants.get(i).getLastName());
                intent.putExtra("phone",etudiants.get(i).getPhone());
                intent.putExtra("filiere", String.valueOf(etudiants.get(i).getFiliere().getId()));
                context.startActivity(intent);
            }
        });
        starViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Do you want to delete this student ?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String id = starViewHolder.id.getText().toString();
                        deleteStudent(id);
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return etudiants.size();
    }
    public class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView username;
        TextView firstname;
        TextView lastname;
        TextView password;
        TextView phone;
        TextView filiere;
        Button deleteBtn;
        Button updateBtn;


        RelativeLayout parent;
        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);
            firstname = itemView.findViewById(R.id.firstname);
            lastname = itemView.findViewById(R.id.lastname);
            phone = itemView.findViewById(R.id.phone);
            filiere = itemView.findViewById(R.id.filiere);
            deleteBtn = itemView.findViewById(R.id.delete);
            updateBtn = itemView.findViewById(R.id.update);

        }
    }
    public  void  updateEtudiants(List<Etudiant> etudiants){
        this.etudiants = etudiants;
        notifyDataSetChanged();
    }
    private void deleteStudent(String id) {
        requestQueue = Volley.newRequestQueue(context);
        String deleteUrl = "http://192.168.1.103:8080/api/student/"+id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                response -> {
                    Toast.makeText(context, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                    loadEtudiants();

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        requestQueue.add(stringRequest);
    }


    public void loadEtudiants(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.103:8080/api/student",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rep", response);
                        Type type = new TypeToken<List<Etudiant>>() {
                        }.getType();
                        updateEtudiants( new Gson().fromJson(response, type));
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