package com.example.proyectointegrador.utils;

import android.app.Application;

import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.model.Participante;

public class MyApp extends Application {
    Grupo grupoSelec;
    Participante loggedParticipante;

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

    public Participante getLoggedParticipante() {
        return loggedParticipante;
    }

    public void setLoggedParticipante(Participante loggedParticipante) {
        this.loggedParticipante = loggedParticipante;
    }
}
