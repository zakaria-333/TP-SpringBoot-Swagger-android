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


import ma.ensa.list.FiliereActivity;
import ma.ensa.list.R;
import ma.ensa.list.UpdateFiliereActivity;
import ma.ensa.list.beans.Filiere;

public class FiliereAdapter extends RecyclerView.Adapter<FiliereAdapter.FiliereViewHolder> {
    private static final String TAG = "FiliereAdapter";
    private List<Filiere> Filieres;
    private Context context;

    private RequestQueue requestQueue ;


    public FiliereAdapter(Context context, List<Filiere> Filieres) {
        this.Filieres = Filieres;
        this.context = context;

    }


    @NonNull
    @Override
    public FiliereViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.filiere_item,
                viewGroup, false);
        final FiliereViewHolder holder = new FiliereViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull FiliereViewHolder starViewHolder, int i) {

        Log.d(TAG, "onBindView call ! "+ i);
        starViewHolder.code.setText(Filieres.get(i).getCode());
        starViewHolder.libelle.setText(Filieres.get(i).getLibelle());
        starViewHolder.id.setText(Filieres.get(i).getId()+"");
        starViewHolder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateFiliereActivity.class);
                intent.putExtra("id",starViewHolder.id.getText().toString());
                intent.putExtra("code",Filieres.get(i).getCode());
                intent.putExtra("libelle",Filieres.get(i).getLibelle());
                context.startActivity(intent);
            }
        });
        starViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Voulez vous supprimé cette filiere ?");
                alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String id = starViewHolder.id.getText().toString();
                        deleteFiliere(id);
                    }
                });
                alertDialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
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
        return Filieres.size();
    }
    public class FiliereViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView code;
        TextView libelle;
        Button deleteBtn;
        Button updateBtn;


        RelativeLayout parent;
        public FiliereViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            code = itemView.findViewById(R.id.code);
            libelle = itemView.findViewById(R.id.libelle);
            deleteBtn = itemView.findViewById(R.id.delete);
            updateBtn = itemView.findViewById(R.id.update);

        }
    }
    public  void  updateFilieres(List<Filiere> Filieres){
        this.Filieres = Filieres;
        notifyDataSetChanged();
    }
    private void deleteFiliere(String id) {
        requestQueue = Volley.newRequestQueue(context);
        String deleteUrl = "http://192.168.1.103:8080/api/v1/filieres/"+id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                response -> {
                    Toast.makeText(context, "Filiere deleted successfully", Toast.LENGTH_SHORT).show();
                    loadFilieres();

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


    public void loadFilieres(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.103:8080/api/v1/filieres",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rep", response);
                        Type type = new TypeToken<List<Filiere>>() {
                        }.getType();
                        updateFilieres( new Gson().fromJson(response, type));
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