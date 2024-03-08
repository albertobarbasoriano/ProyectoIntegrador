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
import com.example.proyectointegrador.view.utils.recyclerview.ParticipanteGrupoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NuevoGrupoActivity extends AppCompatActivity {
    public static final String DB_PATH_GRUPOS = "Grupos";
    private Spinner spnDivisa;
    private Grupo grupo;
    TextInputEditText etTitulo, etDescripcion, etParticipante;
    Button btnAddParticipante;
    ArrayList<String> listaParticipantes;
    ParticipanteGrupoAdapter participanteGrupoAdapter;

    FirebaseDatabase db;
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.label_nuevo_grupo);


        db = FirebaseDatabase.getInstance();
        listaParticipantes = new ArrayList<>();
        grupo = new Grupo(new ArrayList<Gasto>(), listaParticipantes, null, null, null);

        spnDivisa = findViewById(R.id.spnDivisa);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.nuevoGastoCantidades, android.R.layout.simple_spinner_item);
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
            public void onClick(View v) { //Cuando estén guardados usuarios en Firebase (a traves de un login) se checkeara que existen y se cargaran sus datos
                String usernameParticipante = etParticipante.getText().toString().trim();
                if (usernameParticipante.isEmpty()){
                    Toast.makeText(NuevoGrupoActivity.this, R.string.error_user_vacio, Toast.LENGTH_SHORT).show();
                }else{
                    listaParticipantes.add(usernameParticipante);
                    etParticipante.setText("");
                    participanteGrupoAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.mnGuardar){
            guardarGrupo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarGrupo() {
        if (etTitulo.getText().toString().trim().isEmpty() || etDescripcion.getText().toString().trim().isEmpty()){
            Toast.makeText(this, R.string.error_campos_obligatorios, Toast.LENGTH_SHORT).show();
        }else if(listaParticipantes.isEmpty()){
            Toast.makeText(this, R.string.error_no_participantes, Toast.LENGTH_SHORT).show();
        }else{
            //TODO: Antes de guardar el grupo, se debe guardar al usuario que está creando el grupo como participante
            listaParticipantes.add("Creador");
            String key = db.getReference(DB_PATH_GRUPOS).push().getKey();
            grupo.setListaParticipantes(listaParticipantes);
            grupo.setTitulo(etTitulo.getText().toString().trim());
            grupo.setDescripcion(etDescripcion.getText().toString().trim());
            grupo.setDivisa(spnDivisa.getSelectedItem().toString());
            grupo.setKey(key);
            //Subimos el nuevo grupo a la BBDD
            db.getReference(DB_PATH_GRUPOS).child(grupo.getKey()).setValue(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
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
            for (String participante : listaParticipantes){
                reference.child(participante).child("grupos").child(grupo.getKey()).setValue(false);
            }
            listaParticipantes.clear();
            participanteGrupoAdapter.notifyDataSetChanged();

        }
    }
}