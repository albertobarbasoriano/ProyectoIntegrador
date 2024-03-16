package com.example.proyectointegrador.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.proyectointegrador.R;

public class RemoveGastoDialogFragment extends DialogFragment {
    private RemoveGastoDialogListener listener;
    private String key;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater().inflate(R.layout.remove_gasto_dialog_fragment, null);

        builder.setView(v)
                .setTitle(R.string.remove_gasto_title)
                .setCancelable(false)
                .setPositiveButton(R.string.remove_aceptar, null)
                .setNegativeButton(R.string.remove_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                        listener.eliminarGasto(key);
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
        if (context instanceof RemoveGastoDialogListener)
            listener = (RemoveGastoDialogListener) context;
        else
            Log.e("ERROR DialogFragment", "El Activity asociado a dicho fragmento debee implementar RemoveGastoDialogListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (listener != null)
            listener = null;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
