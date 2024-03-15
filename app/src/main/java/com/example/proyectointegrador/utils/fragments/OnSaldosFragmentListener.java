package com.example.proyectointegrador.utils.fragments;

import com.example.proyectointegrador.model.Deuda;

import java.util.ArrayList;

public interface OnSaldosFragmentListener {
    void confirmarCambios(ArrayList<Deuda> deudasAPagar);
}
