package br.com.lampmobile.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import br.com.lampmobile.R;
import br.com.lampmobile.activity.calculadora.CombustivelActivity;
import br.com.lampmobile.helper.CombustivelHelper;
import br.com.lampmobile.model.Historico;

/**
 * Created by lucas.viveiros on 14/09/2016.
 */
public class CombustivelDialogFragment extends DialogFragment {

    private String resultado;

    private Historico historico;

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.label_combustivelXetanol);
        builder.setMessage(resultado)
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CombustivelHelper helper = new CombustivelHelper(getActivity());
                        SQLiteDatabase db = helper.getReadableDatabase();
                        helper.salvarHistorico(db, historico);

                        // RECUPERA HISTÓRICO
                        ((CombustivelActivity)getActivity()).getHistorico();

                        // LIMPA CAMPOS
                        ((CombustivelActivity)getActivity()).limpaCampos();

                    }
                })
                .setNegativeButton(R.string.fechar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CombustivelDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}