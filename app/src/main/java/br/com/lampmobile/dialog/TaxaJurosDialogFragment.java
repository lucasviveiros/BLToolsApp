package br.com.lampmobile.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import br.com.lampmobile.R;
import br.com.lampmobile.activity.calculadora.ImcActivity;
import br.com.lampmobile.helper.ImcHelper;
import br.com.lampmobile.model.Historico;

public class TaxaJurosDialogFragment extends DialogFragment {

    private String titulo;
    private String resultado;

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo);
        builder.setMessage(resultado)
                .setNegativeButton(R.string.fechar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TaxaJurosDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}