package com.example.proyectointegrador.view.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;

import java.util.ArrayList;

public class ParticipanteGrupoAdapter extends RecyclerView.Adapter<ParticipanteGrupoAdapter.ParticipanteGrupoVH> {
    ArrayList<String> participantes;

    public ParticipanteGrupoAdapter(ArrayList<String> participantes) {
        this.participantes = participantes;
    }

    @NonNull
    @Override
    public ParticipanteGrupoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participante_grupo, parent, false);
        ParticipanteGrupoVH pgvh = new ParticipanteGrupoVH(v);
        return pgvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipanteGrupoVH holder, int position) {
        holder.bindItem(participantes.get(position));
    }

    @Override
    public int getItemCount() {
        return participantes.size();
    }

    public static class ParticipanteGrupoVH extends RecyclerView.ViewHolder{
        TextView tvParticipante;
        public ParticipanteGrupoVH(@NonNull View itemView) {
            super(itemView);
            tvParticipante = itemView.findViewById(R.id.tvNombreParticipante);
        }

        public void bindItem(String nombre){tvParticipante.setText(nombre);}
    }
}
