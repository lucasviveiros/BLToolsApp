package br.com.lampmobile.activity.calculadora;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;
import java.util.Map;

import br.com.lampmobile.R;
import br.com.lampmobile.dialog.ChurrascoDialogFragment;
import br.com.lampmobile.helper.ChurrascoHelper;

public class ChurrascoActivity extends AppCompatActivity {

    EditText qntHomens;
    EditText qntMulheres;
    EditText qntCriancas;
    TextView resultado;
    TextView resultadoItem;
    TextView resultadoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churrasco);

        qntHomens = (EditText) findViewById(R.id.churrascoHomens);
        qntMulheres = (EditText) findViewById(R.id.churrascoMulheres);
        qntCriancas = (EditText) findViewById(R.id.churrascoCriancas);

        resultado = (TextView) findViewById(R.id.churrascoResultado);
        resultadoItem = (TextView) findViewById(R.id.churrascoResultadoItem);
        resultadoValor = (TextView) findViewById(R.id.churrascoResultadoValor);

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.churrascoAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment newFragment = new ChurrascoDialogFragment();
        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();;
        Map<ChurrascoHelper.Tipo, List<ChurrascoHelper.Churrasco>> churrascos = helper.getItens(db);

        List<ChurrascoHelper.Churrasco> itens = churrascos.get(ChurrascoHelper.Tipo.CARNE);
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.BEBIDA));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.OUTROS));

        ((ChurrascoDialogFragment)newFragment).criarArrays(itens); //TODO MUDAR PARA ADD TODOS
        newFragment.show(getSupportFragmentManager(), "confChurras");

        return true;
    }

    public void calcular(View view) {
        //Fechar teclado
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();;
        Map<ChurrascoHelper.Tipo, List<ChurrascoHelper.Churrasco>> churrascos = helper.getItensAtivos(db);

        List<ChurrascoHelper.Churrasco> itens = churrascos.get(ChurrascoHelper.Tipo.CARNE);
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.BEBIDA));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.OUTROS));

        resultado.setText("");
        resultadoItem.setText("");
        resultadoValor.setText("");

        int adultos = 0;
        int criancas = 0;
        if (qntHomens.getText() != null && !qntHomens.getText().toString().isEmpty()) {
            adultos = new Integer(qntHomens.getText().toString());
        }

        if (qntMulheres.getText() != null && !qntMulheres.getText().toString().isEmpty()) {
            adultos += new Integer(qntMulheres.getText().toString());
        }

        if (qntCriancas.getText() != null && !qntCriancas.getText().toString().isEmpty()) {
            criancas = new Integer(qntCriancas.getText().toString());
        }

        resultado.setText("Churrasco para " + (adultos > 0 ? adultos + " adultos ": ""));
        if (adultos == 0) {
            resultado.setText(resultado.getText() + (criancas > 0 ? criancas + " crianças.": ""));
        } else {
            resultado.setText(resultado.getText() + (criancas > 0 ? "e " + criancas + " crianças.": ""));
        }

        for (ChurrascoHelper.Churrasco item : itens) {
            resultadoItem.setText(resultadoItem.getText() + item.getItem() + System.getProperty("line.separator"));
            resultadoValor.setText(resultadoValor.getText() + "10 Kg" + System.getProperty("line.separator"));
        }
    }
}
