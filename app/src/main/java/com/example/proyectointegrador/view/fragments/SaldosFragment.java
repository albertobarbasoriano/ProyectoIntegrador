package com.example.proyectointegrador.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectointegrador.databinding.FragmentSaldosBinding;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.view.utils.MyApp;
import com.example.proyectointegrador.view.utils.recyclerview.GastoSaldoAdapter;
import com.example.proyectointegrador.view.utils.recyclerview.ParticipanteSaldoAdapter;

public class SaldosFragment extends Fragment {
    private GastoSaldoAdapter gastoSaldoAdapter;
    private ParticipanteSaldoAdapter participanteSaldoAdapter;
    private LinearLayoutManager llmGastoSaldo, llmParticipanteSaldo;
    private FragmentSaldosBinding binding;
    private Grupo grupo;
    private MyApp app;

    public SaldosFragment() {
    }


    public static SaldosFragment newInstance() {
        SaldosFragment fragment = new SaldosFragment();
        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        app = (MyApp) getActivity().getApplicationContext();
        grupo = app.getGrupoSelec();
        binding = FragmentSaldosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        configurarRVs(root);
        return root;
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


    public void update() {
        grupo = app.getGrupoSelec();
        participanteSaldoAdapter.notifyDataSetChanged();
        gastoSaldoAdapter.notifyDataSetChanged();
    }
}