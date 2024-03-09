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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.view.utils.MyApp;
import com.example.proyectointegrador.view.utils.recyclerview.ParticipanteGastoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NuevoGastoActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spnCantidad;
    private TextInputEditText tietFecha, etTitulo, etCantidad, etPagador;
    private Grupo grupo;
    private Gasto gasto;
    private ArrayList<String> participantesGasto;
    private LinearLayoutManager llm;
    private ParticipanteGastoAdapter participanteGastoAdapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyApp app = (MyApp) getApplicationContext();
        grupo = app.getGrupoSelec();

        participantesGasto = new ArrayList<>();

        spnCantidad = findViewById(R.id.nuevoGastoSpinnerCantidad);
        tietFecha = findViewById(R.id.nuevoGastoFecha);
        etTitulo = findViewById(R.id.etTitulo);
        etCantidad = findViewById(R.id.etCantidad);
        etPagador = findViewById(R.id.etPagador);

        declararGasto();
        setListeners();


        // Creacion
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nuevoGastoCantidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnCantidad.setAdapter(adapter);

        tietFecha.setOnClickListener(this);
    }

    private void setListeners() {
        etPagador.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && etPagador.isEnabled()) {
                    if (grupo.getListaParticipantes().contains(etPagador.getText().toString().trim())) {
                        gasto.setPagador(etPagador.getText().toString().trim());
                        configRV();
                        etPagador.setEnabled(false);

                    } else {
                        Toast.makeText(NuevoGastoActivity.this, R.string.error_participante_no_existe, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        etCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String cantidadSt = etCantidad.getText().toString().trim();
                    if (cantidadSt.isEmpty()) {

                    } else {
                        try {
                            double cantidad = Double.parseDouble(cantidadSt);
                            if (cantidad >= 0) {
                                gasto.setTotal(cantidad);
                            }
                        } catch (NumberFormatException e) {

                        }
                    }
                }
            }
        });
    }

    private void configRV() {
        participanteGastoAdapter = new ParticipanteGastoAdapter(grupo.getListaParticipantes(), gasto);
        rv = findViewById(R.id.rvParticipantesGasto);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(participanteGastoAdapter);
        rv.setHasFixedSize(true);
        participanteGastoAdapter.setCallback(new ParticipanteGastoAdapter.Callback() {
            @Override
            public void onCheckedChanged(String item, boolean isChecked) {
                if (isChecked) {
                    participantesGasto.add(item);
                } else if (participantesGasto.contains(item)) {
                    participantesGasto.remove(item);
                }
                gasto.initParticipantes(participantesGasto);
                participanteGastoAdapter.notifyDataSetChanged();
            }
        });
    }

    private void declararGasto() {
        gasto = new Gasto(participantesGasto, null, 0, ""); //TODO: establecer el usuario loggeado
        gasto.setDivisa(grupo.getDivisa());
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
            guardarGasto();
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarGasto() {
        String titulo = etTitulo.getText().toString().trim();
        String fecha = tietFecha.getText().toString().trim();
        if (gasto.getTotal() == 0){
            String cantidadSt = etCantidad.getText().toString().trim();
            if (cantidadSt.isEmpty()) {

            } else {
                try {
                    double cantidad = Double.parseDouble(cantidadSt);
                    if (cantidad >= 0) {
                        gasto.setTotal(cantidad);
                    }
                } catch (NumberFormatException e) {

                }
            }
        }

        if (titulo.isEmpty() || fecha.isEmpty()){

        }else{
            gasto.setTitulo(titulo);
            gasto.setFecha(fecha);
            gasto.setKey(FirebaseDatabase.getInstance().getReference(NuevoGrupoActivity.DB_PATH_GRUPOS).child(grupo.getKey()).child("listaGastos").push().getKey());
            FirebaseDatabase.getInstance()
                    .getReference(NuevoGrupoActivity.DB_PATH_GRUPOS)
                    .child(grupo.getKey()).child("listaGastos")
                    .child(gasto.getKey())
                    .setValue(gasto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(NuevoGastoActivity.this, R.string.info_gasto_add, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NuevoGastoActivity.this, R.string.error_algo_mal, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nuevoGastoFecha) {

        }
    }
}