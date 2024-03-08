package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.view.utils.recyclerview.GrupoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GruposActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton btn;
    GrupoAdapter grupoAdapter;
    LinearLayoutManager llm;
    RecyclerView rv;
    ArrayList<Grupo> grupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);

        grupos = new ArrayList<>();
        rv = findViewById(R.id.rvGrupos);
        llm = new LinearLayoutManager(this);
        grupoAdapter = new GrupoAdapter(grupos, this);
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
                .child("Creador")
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
                        grupoResult.setListaGastos(new ArrayList<Gasto>());
                        grupos.add(grupoResult);
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
        Intent i = new Intent(GruposActivity.this, GrupoActivity.class);
        i.putExtra("GRUPO", grupo);
        startActivity(i);
    }
}