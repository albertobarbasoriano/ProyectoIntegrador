package com.example.proyectointegrador.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gasto {
    private Map<String, Integer> participantes;
    private String titulo, descripcion, pagador, fecha;
    private double total;

    //CONSTRUCTORES
    public Gasto(List<String> participantes, String titulo, String descripcion, double total, String pagador) {
        initParticipantes(participantes);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.total = total;
        this.pagador = pagador;
    }

    private void initParticipantes(List<String> participantes) {
        this.participantes = new HashMap<>();
        for (String nombre : participantes){
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    //MÉTODOS
    public double calcularPago(){
        return total/participantes.size();
    }

}
