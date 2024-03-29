package com.example.proyectointegrador.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.databinding.FragmentGastosBinding;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.utils.MyApp;
import com.example.proyectointegrador.utils.fragments.OnGastosFragmentListener;
import com.example.proyectointegrador.utils.recyclerview.GastoAdapter;
import com.example.proyectointegrador.view.utils.GrupoViewModel;

import java.util.ArrayList;
import java.util.List;

public class GastosFragment extends Fragment implements View.OnLongClickListener {
    private GastoAdapter adapter;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private FragmentGastosBinding binding;
    private Grupo grupo;
    private OnGastosFragmentListener listener;
    private MyApp app;
    private TextView tvTotalCuenta, tvMiTotalCuenta;

    private GrupoViewModel grupoViewModel;

    public GastosFragment() {
    }

    public static GastosFragment newInstance() {
        GastosFragment fragment = new GastosFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGastosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvTotalCuenta = binding.tvTotalCuenta;
        tvMiTotalCuenta = binding.tvMiTotalCuenta;

        grupoViewModel = new ViewModelProvider(requireActivity()).get(GrupoViewModel.class);
        grupoViewModel.getGrupoSeleccionado().observe(getViewLifecycleOwner(), new Observer<Grupo>() {
            @Override
            public void onChanged(Grupo grupo) {
                update(grupo);
            }
        });

        app = (MyApp) getActivity().getApplication();
        grupo = app.getGrupoSelec();
        configurarRV(root);
        if (grupo.getListaGastos() != null) {
            updateBarraInferior();
        } else {
            tvTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), new Double(0), grupo.formatDivisa()));
            tvMiTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), new Double(0), grupo.formatDivisa()));
        }
        binding.btnAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.launchNuevoGrupo();
            }
        });

        return root;
    }

    public void updateBarraInferior() {

        if (grupo != null) {
            //UPDATE DEL GASTO TOTAL
            List<Gasto> gastos = grupo.getGastoList();
            double totalGrupo = 0;
            if (gastos != null)
                for (Gasto gasto : gastos) {
                    totalGrupo += gasto.getTotal();
                }
            tvTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), totalGrupo, grupo.formatDivisa()));

            //UPDATE DEL GASTO PERSONAL
            double gastoPersonal = 0;
            if (gastos != null)
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
            tvMiTotalCuenta.setText(String.format(getString(R.string.text_coste_gasto), gastoPersonal, grupo.formatDivisa()));
        }


    }


    private void configurarRV(View root) {
        adapter = new GastoAdapter(grupo, this);
        llm = new LinearLayoutManager(root.getContext());
        rv = binding.rvListaGastos;
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
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

    public void update(Grupo grupo) {
        this.grupo = grupo;
        adapter.setGrupo(grupo);
        adapter.notifyDataSetChanged();
        updateBarraInferior();
    }

    @Override
    public boolean onLongClick(View v) {
        Gasto gasto = ((GastoAdapter) rv.getAdapter()).getGrupo().getGastoList().get(rv.getChildAdapterPosition(v));
        Log.i("GastoFragment::onLongClick", "Gasto seleccionado: " + gasto.getTitulo() + ", key: " + gasto.getKey());
        listener.onEliminarGasto(gasto.getKey());
        return true;
    }
}