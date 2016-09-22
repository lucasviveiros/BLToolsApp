package br.com.lampmobile.activity.calculadora;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.lampmobile.R;
import br.com.lampmobile.adapter.historico.ChurrascoHistoricoAdapter;
import br.com.lampmobile.dialog.ChurrascoDialogFragment;
import br.com.lampmobile.dialog.ChurrascoResultadoDialogFragment;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Churrasco;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.utils.Utils;

public class ChurrascoActivity extends CalculadoraActivity {

    EditText qntHomens;
    EditText qntMulheres;
    EditText qntCriancas;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churrasco);

        qntHomens = (EditText) findViewById(R.id.churrascoHomens);
        qntMulheres = (EditText) findViewById(R.id.churrascoMulheres);
        qntCriancas = (EditText) findViewById(R.id.churrascoCriancas);
        // RECUPERA HISTÓRICO
        getHistorico();
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
    public int getCorFundoExclusao() {
        return R.color.colorPrimaryChurrasco;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment newFragment = new ChurrascoDialogFragment();
        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<ChurrascoHelper.Tipo, List<Churrasco>> churrascos = helper.getItens(db);

        List<Churrasco> itens = churrascos.get(ChurrascoHelper.Tipo.CARNE);
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.BEBIDA));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.OUTROS));

        ((ChurrascoDialogFragment) newFragment).criarArrays(itens); //TODO MUDAR PARA ADD TODOS
        newFragment.show(getSupportFragmentManager(), "confChurras");

        return true;
    }

    private void validaPreenchimento() throws Exception {
        if ((qntHomens.getText().toString().isEmpty() || qntHomens.getText().toString().equals("0")) &&
                (qntMulheres.getText().toString().isEmpty() || qntMulheres.getText().toString().equals("0")) &&
                (qntCriancas.getText().toString().isEmpty() || qntCriancas.getText().toString().equals("0"))) {
            throw new Exception();
        }
    }

    @Override
    public void compartilhar(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        Uri uri = Uri.parse("android.resource://br.com.lampmobile/drawable/icone_calculadora.png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "O resuldado do churrasco foi... Baixe já o app http://goo.gl/mR2d");
        startActivity(Intent.createChooser(shareIntent, "Compartilhar"));
    }

    @Override
    public void calcular(View view) {
        //Fechar teclado
        Utils.fecharTeclado(this);

        try {
            validaPreenchimento();
        } catch (Exception e) {
            Toast.makeText(getApplication(), "Favor informar a quantidade de pessoas", Toast.LENGTH_SHORT).show();
            return;
        }

        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<ChurrascoHelper.Tipo, List<Churrasco>> mapChurrasco = helper.getItensAtivos(db);
        List<Churrasco> aux;
        List<Churrasco> itens = new ArrayList<>();
        Churrasco churrascoAux = new Churrasco();

        int contador = 0;
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.CARNE);
        churrascoAux.setItem(ChurrascoHelper.Tipo.CARNE.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("10 kg");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.CARNE, itens);

        contador = 0;
        itens = new ArrayList<>();
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO);
        churrascoAux = new Churrasco();
        churrascoAux.setItem(ChurrascoHelper.Tipo.ACOMPANHAMENTO.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("10 kg");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.ACOMPANHAMENTO, itens);

        contador = 0;
        itens = new ArrayList<>();
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.BEBIDA);
        churrascoAux = new Churrasco();
        churrascoAux.setItem(ChurrascoHelper.Tipo.BEBIDA.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("5 litros");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.BEBIDA, itens);

        contador = 0;
        itens = new ArrayList<>();
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.OUTROS);
        churrascoAux = new Churrasco();
        churrascoAux.setItem(ChurrascoHelper.Tipo.OUTROS.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("50 copos");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.OUTROS, itens);

        ChurrascoResultadoDialogFragment dialog = new ChurrascoResultadoDialogFragment();
        dialog.setMapChurrasco(mapChurrasco);
        dialog.setPessoas(calcularNumeroPessoas());
        dialog.setHistorico(helper.criarHistorico(calcularNumeroPessoas(),
                qntHomens.getText().toString(), qntMulheres.getText().toString(), qntCriancas.getText().toString()));
        dialog.show(getSupportFragmentManager(), "resultChurras");
    }

    @Override
    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    private String calcularNumeroPessoas() {
        String resultado;
        int adultos = 0;
        int criancas = 0;
        if (qntHomens.getText() != null && !qntHomens.getText().toString().isEmpty()) {
            adultos = Integer.valueOf(qntHomens.getText().toString());
        }

        if (qntMulheres.getText() != null && !qntMulheres.getText().toString().isEmpty()) {
            adultos += Integer.valueOf(qntMulheres.getText().toString());
        }

        if (qntCriancas.getText() != null && !qntCriancas.getText().toString().isEmpty()) {
            criancas = Integer.valueOf(qntCriancas.getText().toString());
        }

        resultado = ((adultos > 0 ? adultos + " adultos " : ""));
        if (adultos == 0) {
            resultado = (resultado + (criancas > 0 ? criancas + " crianças." : ""));
        } else {
            resultado = (resultado + (criancas > 0 ? "e " + criancas + " crianças." : ""));
        }

        return resultado;
    }

    @Override
    public void getHistorico() {
        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Historico> historicos = helper.getHistoricos(db);

        if (historicos != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.churrascoHistorico);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            ChurrascoHistoricoAdapter mAdapter = new ChurrascoHistoricoAdapter(this, historicos);
            // specify an adapter (see also next example)
            mRecyclerView.setAdapter(mAdapter);

            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();
        }
    }

    public void setDados(String homens, String mulheres, String criancas) {
        qntHomens.setText(homens);
        qntMulheres.setText(mulheres);
        qntCriancas.setText(criancas);

    }

    public void limpaCampos() {
        qntHomens.setText("");
        qntMulheres.setText("");
        qntCriancas.setText("");

    }
}

