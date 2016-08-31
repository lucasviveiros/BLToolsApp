package br.com.bltoolsapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import br.com.bltoolsapplication.R;
import br.com.bltoolsapplication.helper.ChurrascoHelper;

public class ChurrascoDialogFragment extends DialogFragment {

    List mSelectedItems;
    CharSequence[] itens;
    boolean[] itensChecados;

    /**
     * Metodo para criar os arrays necessários na classe.
     * @param churrascos
     */
    public void criarArrays(List<ChurrascoHelper.Churrasco> churrascos) {
        churrascoToArray(churrascos);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Opções") //TODO add no R.id.
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
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        //TODO SALVAR NO BANCO
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Não faz nada
                    }
                });

        return builder.create();
    }

    private void churrascoToArray(List<ChurrascoHelper.Churrasco> churrascos) {
        itens = new CharSequence[churrascos.size()];
        itensChecados = new boolean[churrascos.size()];

        int i = 0;
        for (ChurrascoHelper.Churrasco churrasco : churrascos) {
            itens[i] = churrasco.getItem();
            itensChecados[i] = churrasco.getAtivo();
            i++;
        }

    }
}
