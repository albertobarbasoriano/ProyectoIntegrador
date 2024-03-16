package com.example.proyectointegrador.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectointegrador.databinding.FragmentSaldosBinding;
import com.example.proyectointegrador.model.Deuda;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.utils.MyApp;
import com.example.proyectointegrador.utils.fragments.OnSaldosFragmentListener;
import com.example.proyectointegrador.utils.recyclerview.GastoSaldoAdapter;
import com.example.proyectointegrador.utils.recyclerview.ParticipanteSaldoAdapter;
import com.example.proyectointegrador.view.utils.GrupoViewModel;

import java.util.ArrayList;

public class SaldosFragment extends Fragment {
    private GastoSaldoAdapter gastoSaldoAdapter;
    private ParticipanteSaldoAdapter participanteSaldoAdapter;
    private LinearLayoutManager llmGastoSaldo, llmParticipanteSaldo;
    private FragmentSaldosBinding binding;
    private Grupo grupo;
    private MyApp app;
    private ArrayList<Deuda> deudasAPagar;
    private Button btnConfirmar;
    private OnSaldosFragmentListener listener;
    private GrupoViewModel grupoViewModel;

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

        app = (MyApp) getActivity().getApplication();
        grupo = app.getGrupoSelec();

        binding = FragmentSaldosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        deudasAPagar = new ArrayList<>();

        grupoViewModel = new ViewModelProvider(requireActivity()).get(GrupoViewModel.class);
        grupoViewModel.getGrupoSeleccionado().observe(getViewLifecycleOwner(), new Observer<Grupo>() {
            @Override
            public void onChanged(Grupo grupo) {
                update(grupo);
            }
        });
        btnConfirmar = binding.btnConfirmar;
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirmarCambios(deudasAPagar);
                btnConfirmar.setVisibility(View.GONE);
            }
        });
        configurarRVs(root);
        return root;
    }


    private void configurarRVs(View root) {
        gastoSaldoAdapter = new GastoSaldoAdapter(grupo);
        gastoSaldoAdapter.setCallback(new GastoSaldoAdapter.Callback() {
            @Override
            public void onCheckedChanged(String participante1, String participante2, boolean isChecked) {
                Deuda deuda = new Deuda(participante1, participante2);
                if (isChecked)
                    deudasAPagar.add(deuda);
                else {
                    int i = 0, indice = 0;
                    for (Deuda check : deudasAPagar) {
                        if (check.getPaga().equals(deuda.getPaga()) && check.getRecibe().equals(deuda.getRecibe())) {
                            indice = i;
                            break;
                        }
                        i++;
                    }
                    deudasAPagar.remove(indice);
                }
                if (!deudasAPagar.isEmpty())
                    btnConfirmar.setVisibility(View.VISIBLE);
                else
                    btnConfirmar.setVisibility(View.GONE);
            }
        });
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSaldosFragmentListener)
            listener = (OnSaldosFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void update(Grupo grupo) {
        this.grupo = grupo;
        participanteSaldoAdapter.setGrupo(grupo);
        gastoSaldoAdapter.setGrupo(grupo);
        participanteSaldoAdapter.notifyDataSetChanged();
        gastoSaldoAdapter.notifyDataSetChanged();
    }
}