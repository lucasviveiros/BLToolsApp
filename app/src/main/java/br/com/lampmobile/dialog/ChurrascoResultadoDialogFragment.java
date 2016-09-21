package br.com.lampmobile.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.lampmobile.R;
import br.com.lampmobile.activity.calculadora.ChurrascoActivity;
import br.com.lampmobile.adapter.ChurrascoResultadoAdapter;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Churrasco;
import br.com.lampmobile.model.Historico;

public class ChurrascoResultadoDialogFragment extends DialogFragment {

    private Map<ChurrascoHelper.Tipo, List<Churrasco>> mapChurrasco;
    private String pessoas;
    private Historico historico;

    public void setPessoas(String pessoas) {
        this.pessoas = pessoas;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public void setMapChurrasco(Map<ChurrascoHelper.Tipo, List<Churrasco>> mapChurrasco) {
        this.mapChurrasco = mapChurrasco;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_resultado_churrasco, null);
        setResultado(view);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(R.string.label_churrasco);
        //builder.setIcon(R.drawable.ic_share_black_24px);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ChurrascoHelper helper = new ChurrascoHelper(getActivity());
                        SQLiteDatabase db = helper.getReadableDatabase();
                        helper.salvarHistorico(db, historico);

                        // RECUPERA HISTÓRICO
                        ((ChurrascoActivity)getActivity()).getHistorico();

                        // LIMPA CAMPOS
                        ((ChurrascoActivity)getActivity()).limpaCampos();
                    }
                })
                .setNegativeButton(R.string.fechar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChurrascoResultadoDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setResultado(View view) {
        List<Churrasco> itens = mapChurrasco.get(ChurrascoHelper.Tipo.CARNE);
        itens.addAll(mapChurrasco.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO));
        itens.addAll(mapChurrasco.get(ChurrascoHelper.Tipo.BEBIDA));
        itens.addAll(mapChurrasco.get(ChurrascoHelper.Tipo.OUTROS));

        ChurrascoResultadoAdapter adapterCarnes = new ChurrascoResultadoAdapter(getContext());
        for (Churrasco item : itens) {
            if (item.getResultado() != null) {
                adapterCarnes.addItem(item);
            } else {
                adapterCarnes.addSeparatorItem(item);
            }
        }
        ListView listViewCarnes = (ListView) view.findViewById(R.id.churrascoResultado);
        listViewCarnes.setAdapter(adapterCarnes);

        TextView numeroPessoas = (TextView) view.findViewById(R.id.churrascoNumeroPessoas);
        numeroPessoas.setText(pessoas);

    }
}
