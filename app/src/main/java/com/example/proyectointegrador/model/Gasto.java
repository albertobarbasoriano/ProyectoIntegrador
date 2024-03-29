package com.example.proyectointegrador.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gasto {
    private Map<String, Integer> participantes;
    private String titulo,  pagador, fecha, divisa, key;
    private double total;

    //CONSTRUCTORES
    public Gasto(List<String> participantesLista, String titulo, double total, String pagador) {
        initParticipantes(participantesLista);
        this.titulo = titulo;
        this.total = total;
        this.pagador = pagador;
    }

    public Gasto() {
    }


    public void initParticipantes(List<String> participantes) {
        this.participantes = new HashMap<>();
        for (String nombre : participantes) {
            this.participantes.put(nombre, 0);
        }
    }

    //GETTERS Y SETTERS
    public Map<String, Integer> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Map<String, Integer> participantes) {
        this.participantes = participantes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagador) {
        this.pagador = pagador;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    //MÉTODOS
    public double calcularPago() {
        return total / (participantes.size() + 1);
    }
    public String formatDivisa() {
        switch (divisa){
            case "EUR":
                return "€";
            case "DOL":
                return "$";
            case "YEN":
                return "¥";
            case "RUB":
                return "₽";
            default:
                return divisa;
        }
    }

}
