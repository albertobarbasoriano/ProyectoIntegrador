package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.utils.MyApp;
import com.example.proyectointegrador.utils.recyclerview.GrupoAdapter;
import com.example.proyectointegrador.view.dialog.DialogListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GruposActivity extends AppCompatActivity implements View.OnClickListener, DialogListener {
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
        if (app.getLoggedParticipante() != null)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnPerfil) {
            Intent i = new Intent(GruposActivity.this, PerfilActivity.class);
            startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == PerfilActivity.RESULT_OK) {
            // Comprueba si el usuario ha cerrado sesión
            boolean logout = data.getBooleanExtra("logout", false);
            if (logout) {
                finish();
            }
        }
    }

    @Override
    public void removeListener(boolean remove) {
        Log.e("caac", "dasfdsfds");
    }
}