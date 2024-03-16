package com.example.proyectointegrador.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Gasto;
import com.example.proyectointegrador.model.Grupo;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoVH> implements View.OnLongClickListener{
    private Grupo grupo;
    private View.OnLongClickListener listener;

    public GastoAdapter(Grupo grupo, View.OnLongClickListener listener){
        this.grupo = grupo;
        this.listener = listener;
    }
    @NonNull
    @Override
    public GastoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        v.setOnLongClickListener(listener);
        GastoVH gvh = new GastoVH(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GastoVH holder, int position) {
        holder.bindItem(grupo.getGastoList().get(position));
    }

    @Override
    public int getItemCount() {
        if (grupo.getGastoList() != null)
            return grupo.getGastoList().size();
        else
            return 0;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    @Override
    public boolean onLongClick(View v) {
        listener.onLongClick(v);
        return true;
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
                tvCosteGasto.setText(String.format(v.getContext().getString(R.string.text_coste_gasto), gasto.getTotal(), gasto.formatDivisa()));
                tvFechaPago.setText(gasto.getFecha());
            }
        }
    }
}
