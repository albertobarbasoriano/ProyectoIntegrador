package com.example.proyectointegrador.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.utils.MyApp;
import com.example.proyectointegrador.utils.dialogs.DatePickerFragment;
import com.example.proyectointegrador.utils.recyclerview.ParticipanteGastoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NuevoGastoActivity extends AppCompatActivity {

    private Spinner spnCantidad, spnPagador;
    private TextInputEditText tietFecha, etTitulo, etCantidad;
    private Grupo grupo;
    private Gasto gasto;
    MyApp app;
    private ArrayList<String> participantesGasto;
    private LinearLayoutManager llm;
    private ParticipanteGastoAdapter participanteGastoAdapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app = (MyApp) getApplicationContext();
        grupo = app.getGrupoSelec();

        participantesGasto = new ArrayList<>();

        spnCantidad = findViewById(R.id.nuevoGastoSpinnerCantidad);
        tietFecha = findViewById(R.id.nuevoGastoFecha);
        etTitulo = findViewById(R.id.etTitulo);
        etCantidad = findViewById(R.id.etCantidad);
        spnPagador = findViewById(R.id.spnPagador);

        declararGasto();
        setListeners();

        // Creación spinner divisa
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{grupo.getDivisa()});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnCantidad.setAdapter(adapter);
        spnCantidad.setEnabled(false);

        //Creación spinner pagador
        String[] participantesArray = grupo.getListaParticipantes().toArray(new String[grupo.getListaParticipantes().size() + 1]);
        participantesArray[participantesArray.length - 1] = getString(R.string.nuevoGastoPagadoHint);
        ArrayAdapter<String> pagadorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, participantesArray) {
            @Override
            public int getCount() {
                return participantesArray.length - 1;
            }
        };
        pagadorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnPagador.setAdapter(pagadorAdapter);
        spnPagador.setSelection(participantesArray.length - 1);
    }

    private void setListeners() {
        spnPagador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gasto.setPagador(spnPagador.getSelectedItem().toString());
                configRV();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkCantidad();
                    if (participanteGastoAdapter != null)
                        participanteGastoAdapter.notifyDataSetChanged();
                }
            }
        });

        tietFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
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
        gasto = new Gasto(participantesGasto, null, 0, "");
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
        if (gasto.getTotal() == 0) {
            checkCantidad();
        } else if (titulo.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(app, R.string.error_campos_obligatorios, Toast.LENGTH_SHORT).show();
        } else if (gasto.getPagador().equals(getString(R.string.nuevoGastoPagadoHint))) {
            Toast.makeText(app, R.string.error_no_pagador, Toast.LENGTH_SHORT).show();
        }else if (participantesGasto.isEmpty()) {
            Toast.makeText(app, R.string.error_no_participantes, Toast.LENGTH_SHORT).show();
        } else {
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
                            if (task.isSuccessful()) {
                                Toast.makeText(NuevoGastoActivity.this, R.string.info_gasto_add, Toast.LENGTH_SHORT).show();
                                Map<String, Gasto> mapaGastos = grupo.getListaGastos();
                                if (mapaGastos == null)
                                    mapaGastos = new HashMap<String, Gasto>();
                                mapaGastos.put(gasto.getKey(), gasto);
                                grupo.setListaGastos(mapaGastos);
                                app.setGrupoSelec(grupo);
                                setResult(RESULT_OK);
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

    private void checkCantidad() {
        String cantidadSt = etCantidad.getText().toString().trim();
        if (cantidadSt.isEmpty()) {
            Toast.makeText(this, R.string.error_no_cantidad, Toast.LENGTH_SHORT).show();
        } else {
            try {
                double cantidad = Double.parseDouble(cantidadSt);
                if (cantidad >= 0) {
                    gasto.setTotal(cantidad);
                } else {
                    Toast.makeText(this, R.string.error_cantidad_0, Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {

            }
        }

    }


    private void showDatePicker() {
        DatePickerFragment dpf = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                tietFecha.setText(fechaSeleccionada);
            }
        });
        dpf.show(getSupportFragmentManager(), "datePicker");
    }
}