package com.example.proyectointegrador.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectointegrador.databinding.FragmentSaldosBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class SaldosFragment extends Fragment {
    private FragmentSaldosBinding binding;

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

        return  root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}