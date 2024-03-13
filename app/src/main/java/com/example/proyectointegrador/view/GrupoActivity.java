package com.example.proyectointegrador.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyectointegrador.view.fragments.GastosFragment;
import com.example.proyectointegrador.view.fragments.SaldosFragment;
import com.example.proyectointegrador.view.utils.MyApp;
import com.example.proyectointegrador.view.utils.fragments.OnGastosFragmentListener;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectointegrador.view.utils.SectionsPagerAdapter;
import com.example.proyectointegrador.databinding.ActivityGrupoBinding;

public class GrupoActivity extends AppCompatActivity implements OnGastosFragmentListener {

    private ActivityGrupoBinding binding;
    private SectionsPagerAdapter sectionsPagerAdapter;

    private MyApp app;
    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        if (sectionsPagerAdapter.getGastosFragment() != null){
                            sectionsPagerAdapter.getGastosFragment().update();
                        }
                        if (sectionsPagerAdapter.getSaldosFragment() != null){
                            sectionsPagerAdapter.getSaldosFragment().update();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrupoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        app = (MyApp) getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(app.getGrupoSelec().getTitulo());

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


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
        startActivityForResult.launch(i);
    }
}