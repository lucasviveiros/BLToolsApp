package br.com.lampmobile.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.lampmobile.R;
import br.com.lampmobile.activity.calculadora.ImcActivity;
import br.com.lampmobile.helper.ImcHelper;
import br.com.lampmobile.model.Historico;

/**
 * Created by lucas.viveiros on 14/09/2016.
 */
public class ImcDialogFragment extends DialogFragment {

    private String resultado;

    private Historico historico;

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    TextView res;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_imc, null);

        res = (TextView) viewDialog.findViewById(R.id.imcDialogResultado);
        res.setText(resultado.toString());

        //builder.setTitle(R.string.label_imc);

        builder.setView(viewDialog)
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ImcHelper helper = new ImcHelper(getActivity());
                        SQLiteDatabase db = helper.getReadableDatabase();
                        helper.salvarHistorico(db, historico);

                        // RECUPERA HISTÓRICO
                        ((ImcActivity) getActivity()).getHistorico();

                        // LIMPA CAMPOS
                        ((ImcActivity) getActivity()).limpaCampos();
                    }
                })
                .setNegativeButton(R.string.fechar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ImcDialogFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}