package com.example.proyectointegrador.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grupo {
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
    public Map<String, Map<String, Double>> getDeudas() {
        Map<String, Map<String, Double>> mapaDeudas = new HashMap<>();
        if (gastoList != null) {
            for (String participante : listaParticipantes) {
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

        }
        return mapaDeudas;
    }

    public static Map<String, Map<String, Double>> simplificarDeudas(Map<String, Map<String, Double>> deudasOriginal) {
        Map<String, Map<String, Double>> deudasSimplificadas = new HashMap<>();

        // Iteramos sobre cada par de participantes
        for (Map.Entry<String, Map<String, Double>> entry : deudasOriginal.entrySet()) {
            String deudor = entry.getKey();
            Map<String, Double> deudasParticipantes = entry.getValue();

            for (Map.Entry<String, Double> deudaEntry : deudasParticipantes.entrySet()) {
                String participante = deudaEntry.getKey();
                double deuda = deudaEntry.getValue();
                if (deudasOriginal.containsKey(participante)) {
                    if (deudasOriginal.get(participante).containsKey(deudor)) {
                        if (deudasOriginal.get(participante).get(deudor) != 0) {//Participante también le debe al deudor
                            double diferenciaDeudas = deuda - deudasOriginal.get(participante).get(deudor);
                            if (diferenciaDeudas > 0) { //Deudor le debe más al participante
                                if (!deudasSimplificadas.containsKey(deudor))
                                    deudasSimplificadas.put(deudor, new HashMap<>());
                                deudasSimplificadas.get(deudor).put(participante, diferenciaDeudas);
                            } else if (diferenciaDeudas < 0) {//Participante le debe más al deudor
                                if (!deudasSimplificadas.containsKey(participante))
                                    deudasSimplificadas.put(participante, new HashMap<>());
                                deudasSimplificadas.get(participante).put(deudor, -diferenciaDeudas); //Lo expresamos como un valor positivo
                            } //Si es 0 no se deben nada
                        }
                    } else {//Participante no le debe nada a deudor
                        if (!deudasSimplificadas.containsKey(deudor))
                            deudasSimplificadas.put(deudor, new HashMap<>());
                        deudasSimplificadas.get(deudor).put(participante, deuda); //La deuda se queda igual
                    }
                } else {
                    if (!deudasSimplificadas.containsKey(deudor))
                        deudasSimplificadas.put(deudor, new HashMap<>());
                    deudasSimplificadas.get(deudor).put(participante, deuda);
                }
            }
        }

        return deudasSimplificadas;
    }


    public int sizeDeudas(Map<String, Map<String, Double>> mapaDeudas) {
        int size = 0;
        if (mapaDeudas != null)
            for (Map.Entry<String, Map<String, Double>> entry : mapaDeudas.entrySet()) {
                size += entry.getValue().size();
            }
        return size;
    }

    public double calcularSaldo(int position) {
        double saldo = 0;
        String participante = listaParticipantes.get(position);
        if (gastoList != null)
            for (Gasto gasto : gastoList) {
                if (gasto.getPagador().equals(participante)) {
                    for (Map.Entry<String, Integer> entry : gasto.getParticipantes().entrySet()) {
                        if (entry.getValue() == 0)
                            saldo += gasto.calcularPago();
                    }
                }
            }

        Map<String, Double> deudas = getDeudas().get(participante);
        if (deudas != null)
            for (Map.Entry<String, Double> entry : deudas.entrySet()) {
                saldo -= entry.getValue();
            }

        return saldo;
    }

    public String formatDivisa() {
        switch (divisa) {
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

    public void addParticipante(Participante participante) {
        listaParticipantes.add(participante.getNombre());
    }

    public void removeParticipante(Participante participante) {
        listaParticipantes.remove(participante.getNombre());
    }

    public void pagarDeudas(String participante1, String participante2) {
        if (listaGastos != null) {
            for (Map.Entry<String, Gasto> entry : listaGastos.entrySet()) {
                Gasto gasto = entry.getValue();
                if (gasto.getPagador().equals(participante2) && gasto.getParticipantes().keySet().contains(participante1)) {
                    gasto.getParticipantes().replace(participante1, 1);
                }
            }
        }
    }


}
