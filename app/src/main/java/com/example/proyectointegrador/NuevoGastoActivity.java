package com.example.proyectointegrador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

public class NuevoGastoActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spnCantidad;
    private TextInputEditText  tietFecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);

        spnCantidad = findViewById(R.id.nuevoGastoSpinnerCantidad);
        tietFecha = findViewById(R.id.nuevoGastoFecha);

        // Creacion
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.nuevoGastoCantidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnCantidad.setAdapter(adapter);

        tietFecha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.nuevoGastoFecha){

        }
    }
}