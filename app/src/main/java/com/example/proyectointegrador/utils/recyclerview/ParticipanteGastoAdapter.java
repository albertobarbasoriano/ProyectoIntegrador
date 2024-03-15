package com.example.proyectointegrador.utils.recyclerview;

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

public class ParticipanteGastoAdapter extends RecyclerView.Adapter<ParticipanteGastoAdapter.ParticipanteGastoVH> {
    private List<String> participantes;
    private Gasto gasto;
    private static Callback callback;

    public ParticipanteGastoAdapter(List<String> participantes, Gasto gasto) {
        this.participantes = participantes;
        this.gasto = gasto;
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
        holder.bindItem(participantes.get(position), gasto);
    }

    @Override
    public int getItemCount() {
        return participantes.size();
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
            if (nombre.equals(gasto.getPagador())) {
                deuda = gasto.calcularPago();
                cb.setChecked(true);
                cb.setEnabled(false);
            }

            if (gasto.getParticipantes().containsKey(nombre)) {
                deuda = gasto.calcularPago();
            }
            tvDeuda.setText(String.format(v.getContext().getString(R.string.text_coste_gasto), deuda, gasto.formatDivisa()));
            if (!nombre.equals(gasto.getPagador()))
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
