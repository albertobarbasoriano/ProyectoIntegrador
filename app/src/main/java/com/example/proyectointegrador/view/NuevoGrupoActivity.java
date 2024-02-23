package com.example.proyectointegrador.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyectointegrador.R;

public class NuevoGrupoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_grupo);
        getSupportActionBar().hide();
    }
}