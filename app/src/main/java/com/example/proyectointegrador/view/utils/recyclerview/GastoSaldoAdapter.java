package com.example.proyectointegrador.view.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Grupo;

import java.util.Map;

public class GastoSaldoAdapter extends RecyclerView.Adapter<GastoSaldoAdapter.GastoSaldoVH> {
    private Grupo grupo;
    private static Callback callback;

    public GastoSaldoAdapter(Grupo grupo) {
        this.grupo = grupo;
    }

    @NonNull
    @Override
    public GastoSaldoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto_saldo, parent, false);
        GastoSaldoVH gsvh = new GastoSaldoVH(v);
        return gsvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GastoSaldoVH holder, int position) {

        Map<String, Map<String, Double>> mapaDeudas = grupo.getDeudas();
        int i = 0;
        for (Map.Entry<String, Map<String, Double>> entry : mapaDeudas.entrySet()){
            for (Map.Entry<String, Double> entry1 : entry.getValue().entrySet()){
                if (position == i) {
                    holder.bindItem(entry.getKey(), entry1.getKey(), entry1.getValue(), grupo.formatDivisa());
                }
                i++;
            }
        }

    }
    @Override
    public int getItemCount() {
        return grupo.sizeDeudas(grupo.getDeudas());
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public interface Callback{
        void onCheckedChanged(String participante1, String participante2, boolean isChecked);
    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static class GastoSaldoVH extends RecyclerView.ViewHolder {
        View v;
        TextView tvInfoDeuda, tvValorDeuda;
        CheckBox checkBox;

        public GastoSaldoVH(@NonNull View itemView) {
            super(itemView);
            tvInfoDeuda = itemView.findViewById(R.id.tvInfoDeuda);
            tvValorDeuda = itemView.findViewById(R.id.tvValorDeuda);
            checkBox = itemView.findViewById(R.id.cbEstadoDeuda);
            v = itemView;
        }

        public void bindItem(String participante1, String participante2, double deuda, String divisa) {
            tvInfoDeuda.setText(String.format(v.getContext().getString(R.string.text_info_deuda), participante1, participante2));
            tvValorDeuda.setText(String.format(v.getContext().getString(R.string.text_coste_gasto), deuda, divisa));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        checkBox.setText(R.string.pagado);
                    else
                        checkBox.setText(R.string.no_pagado);
                    if (callback != null){
                        callback.onCheckedChanged(participante1, participante2, isChecked);
                    }
                }
            });
        }
    }
}
