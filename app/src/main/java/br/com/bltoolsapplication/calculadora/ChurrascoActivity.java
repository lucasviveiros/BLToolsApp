package br.com.bltoolsapplication.calculadora;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.Map;

import br.com.bltoolsapplication.R;
import br.com.bltoolsapplication.dialog.ChurrascoDialogFragment;
import br.com.bltoolsapplication.helper.ChurrascoHelper;

public class ChurrascoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churrasco);
    }

    public void dialogConfiguracao(View view) {
        DialogFragment newFragment = new ChurrascoDialogFragment();
        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();;
        Map<ChurrascoHelper.Tipo, List<ChurrascoHelper.Churrasco>> churrascos = helper.getItens(db);

        ((ChurrascoDialogFragment)newFragment).criarArrays(churrascos.get(ChurrascoHelper.Tipo.CARNE)); //TODO MUDAR PARA ADD TODOS
        newFragment.show(getSupportFragmentManager(), "missiles");
    }
}
