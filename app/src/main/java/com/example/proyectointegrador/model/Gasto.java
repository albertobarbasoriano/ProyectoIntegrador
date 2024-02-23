package com.example.proyectointegrador.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Gasto {
    private String titulo, divisa, fecha;
    private double cantidad;
    private HashMap<Participante, Integer> infoPagos;

    public Gasto(String titulo, String divisa, String fecha, double cantidad, Participante pagador, ArrayList<Participante> participantes) {
        this.titulo = titulo;
        this.divisa = divisa;
        this.fecha = fecha;
        this.cantidad = cantidad;
        iniciarInfoPagos(pagador, participantes);
    }

    private void iniciarInfoPagos(Participante pagador, ArrayList<Participante> participantes) {
        infoPagos = new HashMap<>(participantes.size() + 1);
        infoPagos.put(pagador, 2);
        for (Participante p :
                participantes) {
            infoPagos.put(p, 0);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public HashMap<Participante, Integer> getInfoPagos() {
        return infoPagos;
    }

    public Participante getPagador(){
        Participante pagador = null;
        for (Map.Entry<Participante, Integer> entry :
                infoPagos.entrySet()) {
            if(entry.getValue() == 2){
                pagador = entry.getKey();
                break;
            }
        }
        return pagador;
    }
}
