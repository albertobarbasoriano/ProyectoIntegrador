package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.model.Participante;
import com.example.proyectointegrador.view.utils.MyApp;
import com.example.proyectointegrador.view.utils.recyclerview.GrupoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GruposActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton btn;
    GrupoAdapter grupoAdapter;
    LinearLayoutManager llm;
    RecyclerView rv;
    ArrayList<Grupo> grupos;
    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        grupos = new ArrayList<>();
        rv = findViewById(R.id.rvGrupos);
        llm = new LinearLayoutManager(this);
        grupoAdapter = new GrupoAdapter(grupos, this);
        app = (MyApp) getApplicationContext();
        consultarGrupos();
        rv.setLayoutManager(llm);
        rv.setAdapter(grupoAdapter);
        rv.setHasFixedSize(true);

        btn = findViewById(R.id.btnAddGrupo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GruposActivity.this, NuevoGrupoActivity.class));
            }
        });


    }

    private void consultarGrupos() {
        FirebaseDatabase.getInstance()
                .getReference("Usuarios")
                .child(app.getLoggedParticipante().getUsername())
                .child("grupos")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        grupos.clear();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            addInfoGrupo(childSnapshot.getKey());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GruposActivity.this, R.string.error_algo_mal, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void addInfoGrupo(String key) {
        FirebaseDatabase.getInstance()
                .getReference(NuevoGrupoActivity.DB_PATH_GRUPOS)
                .child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Grupo grupoResult = snapshot.getValue(Grupo.class);
                        if (grupoResult.getListaGastos() == null) {
                            grupoResult.setListaGastos(new HashMap<String, Gasto>());
                        }
                        if (!grupos.isEmpty()) {
                            Grupo update = null;
                            for (Grupo g : grupos) {
                                if (g.getKey().equals(grupoResult.getKey())) {
                                    update = g;
                                    break;
                                }
                            }
                            if (update != null) {
                                int index = grupos.indexOf(update);
                                grupos.set(index, grupoResult);
                                grupos.remove(update);
                            } else {
                                grupos.add(grupoResult);
                            }
                        } else {
                            grupos.add(grupoResult);
                        }
                        grupoAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        Grupo grupo = ((GrupoAdapter) rv.getAdapter()).getGrupos().get(rv.getChildAdapterPosition(v));
        MyApp myApp = (MyApp) getApplicationContext();
        myApp.setGrupoSelec(grupo);
        Intent i = new Intent(GruposActivity.this, GrupoActivity.class);
        startActivity(i);
    }
}