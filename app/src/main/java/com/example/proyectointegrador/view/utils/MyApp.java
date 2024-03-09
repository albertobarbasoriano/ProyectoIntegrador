package com.example.proyectointegrador.view.utils;

import android.app.Application;

import com.example.proyectointegrador.model.Grupo;

public class MyApp extends Application {
    Grupo grupoSelec;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Grupo getGrupoSelec() {
        return grupoSelec;
    }

    public void setGrupoSelec(Grupo grupoSelec) {
        this.grupoSelec = grupoSelec;
    }
}
