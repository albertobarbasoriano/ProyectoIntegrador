package com.example.proyectointegrador.model;

public class Participante {
    String username, nombre, email;

    public Participante(String username, String nombre, String email) {
        this.username = username;
        this.nombre = nombre;
        this.email = email;
    }

    public Participante() {
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
}
