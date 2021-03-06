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
import br.com.lampmobile.activity.calculadora.ConsumoActivity;
import br.com.lampmobile.helper.ConsumoHelper;
import br.com.lampmobile.model.Historico;

/**
 * Created by lucas.viveiros on 14/09/2016.
 */
public class ConsumoDialogFragment extends DialogFragment {

    private String resultado;

    private String resultadoPrincipal;

    private Historico historico;

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setResultadoPrincipal(String resultadoPrincipal) {
        this.resultadoPrincipal = resultadoPrincipal;
    }

    TextView res;
    TextView resPrincipal;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialog_consumo, null);

        res = (TextView) viewDialog.findViewById(R.id.consumoDialogResultado);
        resPrincipal = (TextView) viewDialog.findViewById(R.id.consumoDialogResultadoPrincipal);
        resPrincipal.setText(resultadoPrincipal);
        res.setText(resultado.toString());

        builder.setTitle(R.string.label_consumo);

        builder.setView(viewDialog)
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ConsumoHelper helper = new ConsumoHelper(getActivity());
                        SQLiteDatabase db = helper.getReadableDatabase();
                        helper.salvarHistorico(db, historico);

                        // RECUPERA HISTÓRICO
                        ((ConsumoActivity)getActivity()).getHistorico();

                        // LIMPA CAMPOS
                        ((ConsumoActivity)getActivity()).limpaCampos();
                    }
                })
                .setNegativeButton(R.string.fechar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ConsumoDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}