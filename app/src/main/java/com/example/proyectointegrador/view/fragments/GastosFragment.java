package com.example.proyectointegrador.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.databinding.FragmentGastosBinding;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.model.Participante;
import com.example.proyectointegrador.view.GrupoActivity;
import com.example.proyectointegrador.view.NuevoGastoActivity;
import com.example.proyectointegrador.view.utils.recyclerview.GastoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GastosFragment extends Fragment {
    private GastoAdapter adapter;
    private LinearLayoutManager llm;
    private FragmentGastosBinding binding;
    private Grupo grupo;

    public static GastosFragment newInstance() {
        GastosFragment fragment = new GastosFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGastosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TODO: ESTO HAY QUE BORRARLO
        List<Gasto> gastoList = new ArrayList<>();
        gastoList.add(new Gasto(new ArrayList<>(Arrays.asList(new String[]{"Prueba", "Prueba2"})), "Prueba", "Prueba", 10, "Prueba"));
        gastoList.add(new Gasto(new ArrayList<>(Arrays.asList(new String[]{"Prueba3", "Prueba2"})), "Prueba", "Prueba", 10, "Prueba2"));
        grupo = new Grupo(
                gastoList,
                new ArrayList<>(Arrays.asList(new String[]{"Prueba", "Prueba2"})),
                "Prueba",
                "Prueba",
                "€",
                "123"
        );

        configurarRV(root);
        updateBarraInferior();
        binding.btnAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(), NuevoGastoActivity.class));
            }
        });
        return root;
    }

    private void updateBarraInferior() {
        //UPDATE DEL GASTO TOTAL
        List<Gasto> gastos = grupo.getListaGastos();
        double totalGrupo = 0;
        for(Gasto gasto : gastos){
            totalGrupo += gasto.getTotal();
        }
        binding.tvTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), totalGrupo, grupo.getDivisa()));

        //UPDATE DEL GASTO PERSONAL
        double gastoPersonal = 0;
        for(Gasto gasto: gastos){
            if (gasto.getPagador().equals("Prueba")){ //TODO: Cambiar 'Prueba' por el nombre del usuario loggeado
                gastoPersonal += gasto.calcularPago();
            }else{
                List<String> participantes = new ArrayList<>(gasto.getParticipantes().keySet());
                if (participantes != null){
                    for (String participanteNombre : participantes){
                        if (participanteNombre.equals("Prueba")){ //TODO: Cambiar 'Prueba' por el nombre del usuario loggeado
                            gastoPersonal += gasto.calcularPago();
                            break;
                        }
                    }
                }

            }
        }
        binding.tvMiTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), gastoPersonal, grupo.getDivisa()));
    }

    private void configurarRV(View root) {
        adapter = new GastoAdapter(grupo);
        llm = new LinearLayoutManager(root.getContext());
        binding.rvListaGastos.setLayoutManager(llm);
        binding.rvListaGastos.setAdapter(adapter);
        binding.rvListaGastos.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}