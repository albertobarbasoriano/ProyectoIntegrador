package com.example.proyectointegrador.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyectointegrador.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GruposActivity extends AppCompatActivity {
    FloatingActionButton btn;
    Button btnNavegar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        getSupportActionBar().hide();

        btnNavegar = findViewById(R.id.btnTemporal);
        btn = findViewById(R.id.btnAddGrupo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GruposActivity.this, NuevoGrupoActivity.class));
            }
        });
        btnNavegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GruposActivity.this, GrupoActivity.class));
            }
        });

    }
}