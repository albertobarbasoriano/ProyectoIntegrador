package com.example.proyectointegrador.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Participante;

import java.util.ArrayList;

public class ParticipanteGrupoAdapter extends RecyclerView.Adapter<ParticipanteGrupoAdapter.ParticipanteGrupoVH> {
    ArrayList<Participante> participantes;
    private static Callback callback;

    public ParticipanteGrupoAdapter(ArrayList<Participante> participantes) {
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

    public static interface Callback{
        void OnEliminarParticipante(Participante participante);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public static class ParticipanteGrupoVH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvUsername;
        ImageView btnEliminar;

        public ParticipanteGrupoVH(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreParticipante);
            tvUsername = itemView.findViewById(R.id.tvUsernameParticipante);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bindItem(Participante participante) {
            tvNombre.setText(participante.getNombre());
            tvUsername.setText(participante.getUsername());
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.OnEliminarParticipante(participante);
                }
            });
        }
    }
}
