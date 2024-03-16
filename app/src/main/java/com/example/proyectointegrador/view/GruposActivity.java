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
import com.example.proyectointegrador.utils.dialogs.RemoveGrupoDialogFragment;
import com.example.proyectointegrador.utils.recyclerview.GrupoAdapter;
import com.example.proyectointegrador.utils.dialogs.RemoveGrupoDialogListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GruposActivity extends AppCompatActivity implements View.OnClickListener, RemoveGrupoDialogListener, View.OnLongClickListener {
    FloatingActionButton btn;
    GrupoAdapter grupoAdapter;
    LinearLayoutManager llm;
    RecyclerView rv;
    ArrayList<Grupo> grupos;
    MyApp app;
    FirebaseDatabase db;
    DatabaseReference refLoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        db = FirebaseDatabase.getInstance();

        grupos = new ArrayList<>();
        rv = findViewById(R.id.rvGrupos);
        llm = new LinearLayoutManager(this);
        grupoAdapter = new GrupoAdapter(grupos, this, this);
        app = (MyApp) getApplication();
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
        if (app.getLoggedParticipante() != null){
            refLoggedUser = db.getReference("Usuarios")
                    .child(app.getLoggedParticipante().getUsername())
                    .child("grupos");
            refLoggedUser.addValueEventListener(new ValueEventListener() {
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
    }


    private void addInfoGrupo(String key) {
        db.getReference(NuevoGrupoActivity.DB_PATH_GRUPOS)
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Grupo grupoResult = snapshot.getValue(Grupo.class);
                        if (grupoResult != null) {
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
        app.setGrupoSelec(grupo);
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
    public boolean onLongClick(View v) {
        Grupo grupo = ((GrupoAdapter) rv.getAdapter()).getGrupos().get(rv.getChildAdapterPosition(v));
        Log.i("onLongClick", "Grupo seleccionado: " + grupo.getTitulo());
        RemoveGrupoDialogFragment rgdf = new RemoveGrupoDialogFragment();
        rgdf.setGrupo(grupo);
        rgdf.show(getSupportFragmentManager(), "remove");
        return true;
    }

    public void salirGrupo(Grupo grupo, boolean remove) {
        Log.i("removeListener", "Datos recibidos:" + grupo + ", " + remove);
        if (remove && grupo != null) {
            // Para salir del grupo borramos la referencia del usuario al grupo
            refLoggedUser.child(grupo.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null){
                        Toast.makeText(app, R.string.info_grupo_borrado, Toast.LENGTH_SHORT).show();
                        grupoAdapter.notifyDataSetChanged();
                    }else
                        Toast.makeText(app, R.string.error_algo_mal, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}