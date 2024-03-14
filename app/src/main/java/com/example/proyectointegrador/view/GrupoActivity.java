package com.example.proyectointegrador.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyectointegrador.model.Deuda;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.view.utils.MyApp;
import com.example.proyectointegrador.view.utils.fragments.OnGastosFragmentListener;
import com.example.proyectointegrador.view.utils.fragments.OnSaldosFragmentListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectointegrador.view.utils.SectionsPagerAdapter;
import com.example.proyectointegrador.databinding.ActivityGrupoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class GrupoActivity extends AppCompatActivity implements OnGastosFragmentListener, OnSaldosFragmentListener {

    private ActivityGrupoBinding binding;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private DatabaseReference reference;

    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrupoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (MyApp) getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app.getGrupoSelec().getTitulo());

        reference = FirebaseDatabase.getInstance().getReference("Grupos").child(app.getGrupoSelec().getKey());

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("DDBBUpdate", "En el onDataChange");
                if (snapshot.exists()) {
                    Grupo grupoResult = snapshot.getValue(Grupo.class);
                    app.setGrupoSelec(grupoResult);
                    if (sectionsPagerAdapter.getGastosFragment() != null) {
                        sectionsPagerAdapter.getGastosFragment().update();
                    }
                    if (sectionsPagerAdapter.getSaldosFragment() != null) {
                        sectionsPagerAdapter.getSaldosFragment().update();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void launchNuevoGrupo() {
        Intent i = new Intent(GrupoActivity.this, NuevoGastoActivity.class);
        startActivity(i);
    }

    @Override
    public void confirmarCambios(ArrayList<Deuda> deudasAPagar) {
        Grupo grupo = app.getGrupoSelec();
        for (Deuda deuda : deudasAPagar) {
            grupo.pagarDeudas(deuda.getPaga(), deuda.getRecibe());
        }
        FirebaseDatabase.getInstance().getReference("Grupos")
                .child(grupo.getKey()).child("listaGastos")
                .setValue(grupo.getListaGastos()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(app, "Yupi", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(app, "sadgi", Toast.LENGTH_SHORT).show();
            }
        });
    }


}