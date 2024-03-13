package com.example.proyectointegrador.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.databinding.FragmentGastosBinding;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.view.NuevoGastoActivity;
import com.example.proyectointegrador.view.utils.MyApp;
import com.example.proyectointegrador.view.utils.fragments.OnGastosFragmentListener;
import com.example.proyectointegrador.view.utils.recyclerview.GastoAdapter;

import java.util.ArrayList;
import java.util.List;

public class GastosFragment extends Fragment {
    private GastoAdapter adapter;
    private LinearLayoutManager llm;
    private FragmentGastosBinding binding;
    private Grupo grupo;
    private OnGastosFragmentListener listener;
    private MyApp app;


    public GastosFragment() {}
    public static GastosFragment newInstance() {
        GastosFragment fragment = new GastosFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGastosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        app = (MyApp) getActivity().getApplicationContext();
        grupo = app.getGrupoSelec();
        configurarRV(root);
        if (grupo.getListaGastos() != null){
            updateBarraInferior();
        }else {
            binding.tvTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), new Double(0), grupo.formatDivisa()));
            binding.tvMiTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), new Double(0), grupo.formatDivisa()));
        }
        binding.btnAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchNuevoGrupo();
            }
        });

        return root;
    }

    private void updateBarraInferior() {
        if (grupo != null) {
            //UPDATE DEL GASTO TOTAL
            List<Gasto> gastos = grupo.getGastoList();
            double totalGrupo = 0;
            for (Gasto gasto : gastos) {
                totalGrupo += gasto.getTotal();
            }
            binding.tvTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), totalGrupo, grupo.formatDivisa()));

            //UPDATE DEL GASTO PERSONAL
            double gastoPersonal = 0;
            for (Gasto gasto : gastos) {
                if (gasto.getPagador().equals(app.getLoggedParticipante().getNombre())) {
                    gastoPersonal += gasto.calcularPago();
                } else {
                    List<String> participantes = new ArrayList<>(gasto.getParticipantes().keySet());
                    if (participantes != null) {
                        for (String participanteNombre : participantes) {
                            if (participanteNombre.equals(app.getLoggedParticipante().getNombre())) {
                                gastoPersonal += gasto.calcularPago();
                                break;
                            }
                        }
                    }

                }
            }
            binding.tvMiTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), gastoPersonal, grupo.formatDivisa()));
        }

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnGastosFragmentListener)
            listener = (OnGastosFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void update() {
        grupo = app.getGrupoSelec();
        updateBarraInferior();
        adapter.notifyDataSetChanged();
    }
}