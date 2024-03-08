package com.example.proyectointegrador.model;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private List<Gasto> listaGastos;
    private List<String> listaParticipantes;
    private String titulo, descripcion, divisa, key;

    public Grupo(List<Gasto> listaGastos, List<String> listaParticipantes, String titulo, String descripcion, String divisa, String key) {
        this.listaGastos = listaGastos;
        this.listaParticipantes = listaParticipantes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.divisa = divisa;
        this.key = key;
    }

    public List<Gasto> getListaGastos() {
        return listaGastos;
    }

    public void setListaGastos(List<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }

    public List<String> getListaParticipantes() {
        return listaParticipantes;
    }

    public void setListaParticipantes(List<String> listaParticipantes) {
        this.listaParticipantes = listaParticipantes;
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
}
