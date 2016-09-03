package br.com.lampmobile.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.lampmobile.R;
import br.com.lampmobile.adapter.ChurrascoResultadoAdapter;
import br.com.lampmobile.helper.ChurrascoHelper;

public class ChurrascoResultadoDialogFragment extends DialogFragment {

    private Map<ChurrascoHelper.Tipo, List<ChurrascoHelper.Churrasco>> mapChurrasco;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_resultado_churrasco, null);
        setResultado(view);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.fechar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChurrascoResultadoDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setMapChurrasco(Map<ChurrascoHelper.Tipo, List<ChurrascoHelper.Churrasco>> mapChurrasco) {
        this.mapChurrasco = mapChurrasco;
    }

    public void setResultado(View view) {
        ChurrascoResultadoAdapter adapterCarnes = new ChurrascoResultadoAdapter(getActivity(), mapChurrasco.get(ChurrascoHelper.Tipo.CARNE));
        ListView listViewCarnes = (ListView) view.findViewById(R.id.churrascoCarnesResultado);
        listViewCarnes.setAdapter(adapterCarnes);

        ChurrascoResultadoAdapter adapterAcompanhamentos = new ChurrascoResultadoAdapter(getActivity(), mapChurrasco.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO));
        ListView listViewAcompanhamentos = (ListView) view.findViewById(R.id.churrascoAcompanhamentosResultado);
        listViewAcompanhamentos.setAdapter(adapterAcompanhamentos);

        ChurrascoResultadoAdapter adapterOutrosmentos = new ChurrascoResultadoAdapter(getActivity(), mapChurrasco.get(ChurrascoHelper.Tipo.OUTROS));
        ListView listViewOutros = (ListView) view.findViewById(R.id.churrascoOutrosResultado);
        listViewOutros.setAdapter(adapterOutrosmentos);

        ChurrascoResultadoAdapter adapterBebidas = new ChurrascoResultadoAdapter(getActivity(), mapChurrasco.get(ChurrascoHelper.Tipo.BEBIDA));
        ListView listViewBebidas = (ListView) view.findViewById(R.id.churrascoBebidasResultado);
        listViewBebidas.setAdapter(adapterBebidas);
    }
}
