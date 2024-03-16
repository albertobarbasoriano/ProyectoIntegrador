package com.example.proyectointegrador.view.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectointegrador.model.Grupo;

public class GrupoViewModel extends ViewModel {
    private MutableLiveData<Grupo> grupoSeleccionado = new MutableLiveData<>();

    public LiveData<Grupo> getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(Grupo grupo) {
        grupoSeleccionado.setValue(grupo);
    }
}
