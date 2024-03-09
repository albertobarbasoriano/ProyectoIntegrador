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
import com.example.proyectointegrador.model.Gasto;

import java.util.List;

import javax.security.auth.callback.Callback;

public class ParticipanteGastoAdapter extends RecyclerView.Adapter<ParticipanteGastoAdapter.ParticipanteGastoVH> {
    private List<String> participantes;
    private Gasto gasto;
    private static Callback callback;

    public ParticipanteGastoAdapter(List<String> participantes, Gasto gasto) {
        this.participantes = participantes;
        this.gasto = gasto;
        participantes.remove(gasto.getPagador());
    }

    @NonNull
    @Override
    public ParticipanteGastoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participante_gasto, parent, false);
        ParticipanteGastoVH pgvh = new ParticipanteGastoVH(v);
        return pgvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipanteGastoVH holder, int position) {
        if (!participantes.get(position).equals(gasto.getPagador()))
            holder.bindItem(participantes.get(position), gasto);
    }

    @Override
    public int getItemCount() {
        return participantes.size() ;
    }

    public List<String> getParticipantes() {
        return participantes;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onCheckedChanged(String item, boolean isChecked);
    }

    public static class ParticipanteGastoVH extends RecyclerView.ViewHolder {
        CheckBox cb;
        TextView tvNombre, tvDeuda;
        View v;

        public ParticipanteGastoVH(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cbParticipaGasto);
            tvNombre = itemView.findViewById(R.id.tvNombreParticipante);
            tvDeuda = itemView.findViewById(R.id.tvDeudaParticipante);
            v = itemView;
        }


        void bindItem(String nombre, Gasto gasto) {
            tvNombre.setText(nombre);
            double deuda = 0;
            if (gasto.getParticipantes().containsKey(nombre)){
                deuda = gasto.calcularPago();
                cb.setChecked(true);
            }else{
                cb.setChecked(false);
            }
            tvDeuda.setText(String.format(v.getContext().getString(R.string.text_coste_gasto), deuda, gasto.formatDivisa()));
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (callback != null) {
                        callback.onCheckedChanged(nombre, isChecked);
                    }
                }
            });
        }
    }
}
