package com.example.proyectointegrador.model;

import java.util.ArrayList;
import java.util.List;

public class Participante {
    String username, nombre, email;
    List<String> grupos;

    public Participante(String username, String nombre, String email) {
        this.username = username;
        this.nombre = nombre;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }
}
