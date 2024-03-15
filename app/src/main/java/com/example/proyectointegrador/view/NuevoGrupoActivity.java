package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.model.Participante;
import com.example.proyectointegrador.utils.MyApp;
import com.example.proyectointegrador.utils.recyclerview.ParticipanteGrupoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NuevoGrupoActivity extends AppCompatActivity {
    public static final String DB_PATH_GRUPOS = "Grupos";
    private Spinner spnDivisa;
    private Grupo grupo;
    TextInputEditText etTitulo, etDescripcion, etParticipante;
    Button btnAddParticipante;
    ArrayList<Participante> listaParticipantes;
    ParticipanteGrupoAdapter participanteGrupoAdapter;
    FirebaseDatabase db;
    RecyclerView rv;
    MyApp app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseDatabase.getInstance();
        listaParticipantes = new ArrayList<>();
        grupo = new Grupo(new ArrayList<Gasto>(), new ArrayList<String>(), null, null, null);
        app = (MyApp) getApplicationContext();

        spnDivisa = findViewById(R.id.spnDivisa);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nuevoGastoCantidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnDivisa.setAdapter(adapter);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etParticipante = findViewById(R.id.etParticipante);
        btnAddParticipante = findViewById(R.id.btnAddParticipante);

        participanteGrupoAdapter = new ParticipanteGrupoAdapter(listaParticipantes);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv = findViewById(R.id.rvParticipantesGrupo);
        rv.setLayoutManager(llm);
        rv.setAdapter(participanteGrupoAdapter);
        rv.setHasFixedSize(true);

        btnAddParticipante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameParticipante = etParticipante.getText().toString().trim();
                if (usernameParticipante.isEmpty()) {
                    Toast.makeText(NuevoGrupoActivity.this, R.string.error_user_vacio, Toast.LENGTH_SHORT).show();
                }else if(usernameParticipante.equals(app.getLoggedParticipante().getUsername())){
                    Toast.makeText(app, R.string.error_creador_added, Toast.LENGTH_SHORT).show();
                } else { //Comprobación de que el usuario existe en Firebase
                    FirebaseDatabase.getInstance()
                            .getReference("Usuarios")
                            .child(usernameParticipante)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Participante participante = snapshot.getValue(Participante.class);
                                        if (comprobarParticipante(participante)) {
                                            Toast.makeText(app, R.string.error_participante_added, Toast.LENGTH_SHORT).show();
                                        }else {
                                            listaParticipantes.add(participante);
                                            grupo.addParticipante(participante);
                                            etParticipante.setText("");
                                            participanteGrupoAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        Toast.makeText(NuevoGrupoActivity.this, R.string.error_participante_no_existe_ddbb, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
        });


    }

    private boolean comprobarParticipante(Participante participante) {
        for (Participante current : listaParticipantes){
            if (current.getUsername().equals(participante.getUsername())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.mnGuardar) {
            guardarGrupo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarGrupo() {
        if (etTitulo.getText().toString().trim().isEmpty() || etDescripcion.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.error_campos_obligatorios, Toast.LENGTH_SHORT).show();
        } else if (listaParticipantes.isEmpty()) {
            Toast.makeText(this, R.string.error_no_participantes, Toast.LENGTH_SHORT).show();
        } else {
            grupo.addParticipante(app.getLoggedParticipante());
            listaParticipantes.add(app.getLoggedParticipante());
            String key = db.getReference(DB_PATH_GRUPOS).push().getKey();
            grupo.setTitulo(etTitulo.getText().toString().trim());
            grupo.setDescripcion(etDescripcion.getText().toString().trim());
            grupo.setDivisa(spnDivisa.getSelectedItem().toString());
            grupo.setKey(key);
            //Subimos el nuevo grupo a la BBDD
            db.getReference(DB_PATH_GRUPOS).child(grupo.getKey()).setValue(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(NuevoGrupoActivity.this, String.format(getString(R.string.info_grupo_add), grupo.getTitulo()), Toast.LENGTH_SHORT).show();
                        etTitulo.setText("");
                        etDescripcion.setText("");
                        spnDivisa.setSelection(0);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NuevoGrupoActivity.this, R.string.error_algo_mal, Toast.LENGTH_SHORT).show();
                }
            });
            //Actualizamos los grupos a los que pertenecen los usuarios
            DatabaseReference reference = db.getReference("Usuarios");
            for (Participante participante : listaParticipantes) {
                reference.child(participante.getUsername()).child("grupos").child(grupo.getKey()).setValue(false);
            }
            listaParticipantes.clear();
            participanteGrupoAdapter.notifyDataSetChanged();

        }
    }
}