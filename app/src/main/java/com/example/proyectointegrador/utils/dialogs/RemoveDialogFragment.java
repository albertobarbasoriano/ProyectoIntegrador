package com.example.proyectointegrador.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.model.Grupo;

public class RemoveDialogFragment extends DialogFragment {

    DialogListener listener;
    private Grupo grupo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater().inflate(R.layout.remove_dialog_fragment, null);

        builder.setView(v);
        builder.setTitle(R.string.removeTitle)
                .setCancelable(false)
                .setPositiveButton(R.string.removeAceptar, null)
                .setNegativeButton(R.string.removeCancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog ad = builder.create();
        ad.setCanceledOnTouchOutside(false);

        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btn = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("RemoveDialogFragment", "Datos enviados: " + grupo.getTitulo() + true);
                        listener.removeListener(grupo, true);
                        dismiss();
                    }
                });
            }
        });

        return ad;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener) {
            listener = (DialogListener) context;
        } else {
            Log.e("ERROR DialogFragment",
                    "El Activity asociado a dicho fragmento debe implementar OnDatosDialogListener ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (listener != null) {
            listener = null;
        }
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}