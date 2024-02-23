package com.example.proyectointegrador.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectointegrador.databinding.FragmentGastosBinding;
import com.example.proyectointegrador.view.GrupoActivity;
import com.example.proyectointegrador.view.NuevoGastoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GastosFragment extends Fragment {

    FloatingActionButton btn;
    private GastosViewModel mViewModel;
    private FragmentGastosBinding binding;

    public static GastosFragment newInstance() {
        GastosFragment fragment = new GastosFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGastosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnAddGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(), NuevoGastoActivity.class));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}