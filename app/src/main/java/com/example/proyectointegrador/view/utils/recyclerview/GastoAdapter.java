package com.example.proyectointegrador.view.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoVH> {
    private static Grupo grupo;

    public GastoAdapter(Grupo grupo){
        this.grupo = grupo;
    }
    @NonNull
    @Override
    public GastoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        GastoVH gvh = new GastoVH(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GastoVH holder, int position) {
        holder.bindItem(grupo.getGastoList().get(position));
    }

    @Override
    public int getItemCount() {
        return grupo.getGastoList().size();
    }

    public static class GastoVH extends RecyclerView.ViewHolder{
       TextView tvTituloGasto, tvPagadoPor, tvCosteGasto, tvFechaPago;
        private View v;
        public GastoVH(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            tvTituloGasto = v.findViewById(R.id.tvTituloGasto);
            tvPagadoPor = v.findViewById(R.id.tvPagadoPor);
            tvCosteGasto = v.findViewById(R.id.tvCosteGasto);
            tvFechaPago = v.findViewById(R.id.tvFechaPago);
        }
        public void bindItem(Gasto gasto){
            if(gasto != null){
                tvTituloGasto.setText(gasto.getTitulo());
                tvPagadoPor.setText(String.format(v.getContext().getString(R.string.text_pagado_por), gasto.getPagador()));
                tvCosteGasto.setText(String.format(v.getContext().getString(R.string.text_coste_gasto), gasto.getTotal(), grupo.formatDivisa()));
                tvFechaPago.setText(gasto.getFecha());
            }
        }
    }
}
