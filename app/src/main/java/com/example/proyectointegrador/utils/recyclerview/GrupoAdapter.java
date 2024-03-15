package com.example.proyectointegrador.utils.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Grupo;
import com.example.proyectointegrador.utils.dialogs.RemoveDialogFragment;

import java.util.ArrayList;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoVH> implements View.OnClickListener, View.OnLongClickListener {

    ArrayList<Grupo> grupos;
    View.OnClickListener listener;
    View.OnLongClickListener onLongClickListener;

    public GrupoAdapter(ArrayList<Grupo> grupos, View.OnClickListener listener, View.OnLongClickListener onLongClickListener) {
        this.grupos = grupos;
        this.listener = listener;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public GrupoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grupo, parent, false);
        v.setOnClickListener(listener);
        v.setOnLongClickListener(onLongClickListener);
        GrupoVH gvh = new GrupoVH(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoVH holder, int position) {
        holder.bindItem(grupos.get(position));
    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

    @Override
    public boolean onLongClick(View v) {
        onLongClickListener.onLongClick(v);
        return true;
    }

    public static  class GrupoVH extends RecyclerView.ViewHolder{
        TextView tvNombre,tvDescripcion;
        public GrupoVH(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreGrupo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionGrupo);
        }

        public void bindItem(Grupo grupo){
            tvNombre.setText(grupo.getTitulo());
            tvDescripcion.setText(grupo.getDescripcion());
        }

    }
}
