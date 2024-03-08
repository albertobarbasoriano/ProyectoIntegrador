package com.example.proyectointegrador.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectointegrador.databinding.FragmentSaldosBinding;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.view.utils.recyclerview.GastoSaldoAdapter;
import com.example.proyectointegrador.view.utils.recyclerview.ParticipanteSaldoAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SaldosFragment extends Fragment {
    private GastoSaldoAdapter gastoSaldoAdapter;
    private ParticipanteSaldoAdapter participanteSaldoAdapter;
    private LinearLayoutManager llmGastoSaldo, llmParticipanteSaldo;
    private FragmentSaldosBinding binding;
    private Grupo grupo;

    public static SaldosFragment newInstance() {
        SaldosFragment fragment = new SaldosFragment();

        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentSaldosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TODO: ESTO HAY QUE BORRARLO
        List<Gasto> gastoList = new ArrayList<>();
        gastoList.add(new Gasto(new ArrayList<>(Arrays.asList(new String[]{"Prueba", "Prueba2"})), "Prueba", "Prueba", 10, "Prueba3"));
        gastoList.add(new Gasto(new ArrayList<>(Arrays.asList(new String[]{"Prueba3", "Prueba2"})), "Prueba", "Prueba", 10, "Prueba"));
        grupo = new Grupo(
                gastoList,
                new ArrayList<>(Arrays.asList(new String[]{"Prueba", "Prueba2", "Prueba3"})),
                "Prueba",
                "Prueba",
                "€",
                "123"
        );

        configurarRVs(root);

        return  root;
    }

    private void configurarRVs(View root) {
        gastoSaldoAdapter = new GastoSaldoAdapter(grupo);
        participanteSaldoAdapter = new ParticipanteSaldoAdapter(grupo);
        llmGastoSaldo = new LinearLayoutManager(root.getContext());
        llmParticipanteSaldo = new LinearLayoutManager(root.getContext());
        binding.rvResumenGastos.setLayoutManager(llmGastoSaldo);
        binding.rvResumenGastos.setAdapter(gastoSaldoAdapter);
        binding.rvResumenGastos.setHasFixedSize(true);
        binding.rvSaldoParticipantes.setLayoutManager(llmParticipanteSaldo);
        binding.rvSaldoParticipantes.setAdapter(participanteSaldoAdapter);
        binding.rvSaldoParticipantes.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}