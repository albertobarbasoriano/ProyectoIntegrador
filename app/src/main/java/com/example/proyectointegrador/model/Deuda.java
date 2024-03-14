package com.example.proyectointegrador.model;

public class Deuda {
    private String paga, recibe;

    public Deuda(String paga, String recibe) {
        this.paga = paga;
        this.recibe = recibe;
    }

    public String getPaga() {
        return paga;
    }

    public String getRecibe() {
        return recibe;
    }
}
