package com.example.proyectointegrador.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grupo implements Parcelable{
    private List<Gasto> gastoList;
    private Map<String, Gasto> listaGastos;
    private List<String> listaParticipantes;
    private String titulo, descripcion, divisa, key;

    public Grupo() {
    }

    public Grupo(List<Gasto> listaGastos, List<String> listaParticipantes, String titulo, String descripcion, String divisa) {
        this.gastoList = listaGastos;
        this.listaParticipantes = listaParticipantes;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.divisa = divisa;
    }


    protected Grupo(Parcel in) {
        gastoList = in.createTypedArrayList(Gasto.CREATOR);
        listaParticipantes = in.createStringArrayList();
        titulo = in.readString();
        descripcion = in.readString();
        divisa = in.readString();
        key = in.readString();
    }

    public static final Creator<Grupo> CREATOR = new Creator<Grupo>() {
        @Override
        public Grupo createFromParcel(Parcel in) {
            return new Grupo(in);
        }

        @Override
        public Grupo[] newArray(int size) {
            return new Grupo[size];
        }
    };

    public List<Gasto> getGastoList() {
        return gastoList;
    }

    public void setGastoList(List<Gasto> gastoList) {
        this.gastoList = gastoList;
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

    public Map<String, Gasto> getListaGastos() {
        return listaGastos;
    }

    public void setListaGastos(Map<String, Gasto> listaGastos) {
        this.listaGastos = listaGastos;
        gastoList = new ArrayList<>(listaGastos.values());
    }

    //MÉTODOS
    public Map<String, Map<String, Double>> getDeudas(){
        Map<String, Map<String, Double>> mapaDeudas = new HashMap<>();
        for (String participante : listaParticipantes){
            Map<String, Double> deudasParticipante = new HashMap<>();
            for (Gasto gasto : gastoList) {
                if (gasto.getParticipantes().containsKey(participante) && gasto.getParticipantes().get(participante) == 0) {
                    if (deudasParticipante.containsKey(gasto.getPagador())) {
                        double tmp = deudasParticipante.get(gasto.getPagador());
                        deudasParticipante.replace(gasto.getPagador(), tmp + gasto.calcularPago());
                    } else {
                        deudasParticipante.put(gasto.getPagador(), gasto.calcularPago());
                    }
                }
            }
            mapaDeudas.put(participante, deudasParticipante);
        }
        return mapaDeudas;
    }

    public int sizeDeudas(Map<String, Map<String, Double>> mapaDeudas){
        int size = 0;
        for (Map.Entry<String, Map<String, Double>> entry : mapaDeudas.entrySet()){
            size += entry.getValue().size();
        }
        return size;
    }

    public double calcularSaldo(int position) {
        double saldo = 0;
        String participante = listaParticipantes.get(position);
        for(Gasto gasto : gastoList){
            if (gasto.getPagador().equals(participante)){
                saldo += gasto.getTotal();
            }
        }
       Map<String, Double> deudas = getDeudas().get(participante);

        for (Map.Entry<String, Double> entry : deudas.entrySet()){
            saldo -= entry.getValue();
        }

        return saldo;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(gastoList);
        dest.writeStringList(listaParticipantes);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(divisa);
        dest.writeString(key);
    }
}
