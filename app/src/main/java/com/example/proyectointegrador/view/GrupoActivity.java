package com.example.proyectointegrador.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Deuda;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.utils.MyApp;
import com.example.proyectointegrador.utils.dialogs.RemoveGastoDialogFragment;
import com.example.proyectointegrador.utils.dialogs.RemoveGastoDialogListener;
import com.example.proyectointegrador.utils.fragments.OnGastosFragmentListener;
import com.example.proyectointegrador.utils.fragments.OnSaldosFragmentListener;
import com.example.proyectointegrador.view.utils.GrupoViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectointegrador.utils.SectionsPagerAdapter;
import com.example.proyectointegrador.databinding.ActivityGrupoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GrupoActivity extends AppCompatActivity implements OnGastosFragmentListener, OnSaldosFragmentListener, RemoveGastoDialogListener {

    private ActivityGrupoBinding binding;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private DatabaseReference reference;
    private GrupoViewModel grupoViewModel;
    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrupoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        grupoViewModel = new ViewModelProvider(this).get(GrupoViewModel.class);

        app = (MyApp) getApplication();

        grupoViewModel.setGrupoSeleccionado(app.getGrupoSelec());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i("GruposActivity::onCreate", "Grupo selec:" + app.getGrupoSelec());
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
                if (snapshot.exists()) {
                    Grupo grupoResult = snapshot.getValue(Grupo.class);
                    app.setGrupoSelec(grupoResult);
                    grupoViewModel.setGrupoSeleccionado(grupoResult);
//                    if (sectionsPagerAdapter.getGastosFragment() != null) {
//                        grupoViewModel.setGrupoSeleccionado(grupoResult);
//                        sectionsPagerAdapter.getGastosFragment().update(grupoResult);
//                        sectionsPagerAdapter.getGastosFragment().updateBarraInferior();
//                    }
//                    if (sectionsPagerAdapter.getSaldosFragment() != null) {
//                        grupoViewModel.setGrupoSeleccionado(grupoResult);
//                        sectionsPagerAdapter.getSaldosFragment().update(grupoResult);
//                    }
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

    public void setGrupoSeleccionado(Grupo grupo) {
        grupoViewModel.setGrupoSeleccionado(grupo);
    }

    @Override
    public void launchNuevoGrupo() {
        Intent i = new Intent(GrupoActivity.this, NuevoGastoActivity.class);
        startActivity(i);
    }

    @Override
    public void onEliminarGasto(String key) {
        RemoveGastoDialogFragment rgdf = new RemoveGastoDialogFragment();
        rgdf.setKey(key);
        rgdf.show(getSupportFragmentManager(), "removeGasto");
    }

    @Override
    public void confirmarCambios(ArrayList<Deuda> deudasAPagar) {
        Grupo grupo = app.getGrupoSelec();
        for (Deuda deuda : deudasAPagar) { //Como las deudas están simplificadas, al confirmar pagamos las deudas en ambos sentidos
            grupo.pagarDeudas(deuda.getPaga(), deuda.getRecibe());
            grupo.pagarDeudas(deuda.getRecibe(), deuda.getPaga());
        }
        FirebaseDatabase.getInstance().getReference("Grupos")
                .child(grupo.getKey()).child("listaGastos")
                .setValue(grupo.getListaGastos()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(app, R.string.info_operacion_completada, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(app, R.string.error_algo_mal, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void eliminarGasto(String key) {
        reference.child("listaGastos/" + key).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null)
                    Toast.makeText(app, R.string.info_gasto_eliminado, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(app, R.string.error_algo_mal, Toast.LENGTH_SHORT).show();
            }
        });
    }
}