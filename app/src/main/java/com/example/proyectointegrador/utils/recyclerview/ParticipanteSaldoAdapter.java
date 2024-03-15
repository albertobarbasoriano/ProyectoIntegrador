package com.example.proyectointegrador.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Grupo;

public class ParticipanteSaldoAdapter extends RecyclerView.Adapter<ParticipanteSaldoAdapter.ParticipanteSaldoVH> {

    private Grupo grupo;

    public ParticipanteSaldoAdapter(Grupo grupo) {
        this.grupo = grupo;
    }

    @NonNull
    @Override
    public ParticipanteSaldoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_participante_saldo, parent, false);
        ParticipanteSaldoVH psvh = new ParticipanteSaldoVH(v);
        return psvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipanteSaldoVH holder, int position) {
        holder.bindItem(grupo.getListaParticipantes().get(position), grupo.calcularSaldo(position), grupo.formatDivisa());
    }

    @Override
    public int getItemCount() {
        return grupo.getListaParticipantes().size();
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public static class ParticipanteSaldoVH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvSaldo;
        View v;

        public ParticipanteSaldoVH(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            tvNombre = itemView.findViewById(R.id.tvNombreParticipante);
            tvSaldo = itemView.findViewById(R.id.tvBalanceSaldo);
        }

        public void bindItem(String nombre, double saldo, String divisa) {
            tvNombre.setText(nombre);
            String saldoString = String.format(v.getContext().getString(R.string.text_coste_gasto), saldo, divisa);
            if (saldo > 0) {
                saldoString = "+" + saldoString;
                tvSaldo.setTextColor(v.getContext().getColor(R.color.verde_principal));
            }else if(saldo == 0){
                tvSaldo.setTextColor(v.getContext().getColor(R.color.black));
            }else {
                tvSaldo.setTextColor(v.getContext().getColor(R.color.rojo));
            }
            tvSaldo.setText(saldoString);
        }
    }
}
