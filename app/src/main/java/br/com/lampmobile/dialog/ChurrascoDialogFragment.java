package br.com.lampmobile.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Churrasco;

public class ChurrascoDialogFragment extends DialogFragment {

    List<Churrasco> churrascos;
    List mSelectedItems;
    CharSequence[] itens;
    boolean[] itensChecados;

    /**
     * Metodo para criar os arrays necessários na classe.
     *
     * @param churrascos
     */
    public void criarArrays(List<Churrasco> churrascos) {
        this.churrascos = churrascos;
        churrascoToArray(churrascos);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.selecione_itens)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(itens, itensChecados,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remover it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int i = 0;
                        for (Churrasco churrasco : churrascos) {
                            churrasco.setAtivo(itensChecados[i]);
                            i++;
                        }

                        ChurrascoHelper helper = new ChurrascoHelper(getActivity());
                        SQLiteDatabase db = helper.getReadableDatabase();
                        helper.atualizarStatus(db, churrascos);


                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    private void churrascoToArray(List<Churrasco> churrascos) {
        itens = new CharSequence[churrascos.size()];
        itensChecados = new boolean[churrascos.size()];

        int i = 0;
        for (Churrasco churrasco : churrascos) {
            itens[i] = churrasco.getItem();
            itensChecados[i] = churrasco.getAtivo();
            i++;
        }

    }
}
