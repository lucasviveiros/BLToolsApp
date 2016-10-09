package br.com.lampmobile.activity.calculadora;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.math.BigDecimal;
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

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.icone_calculadora);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "O resuldado do churrasco foi..." + "\n\nLink : " + "http://goo.gl/mR2d");
        String url = MediaStore.Images.Media.insertImage(this.getContentResolver(), bm, "iconeLamp", "description");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

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
        for (Churrasco c : aux) {
            if(c.getAtivo()){
                contador++;
            }
        }
        if(contador > 0){
            churrascoAux.setItem(ChurrascoHelper.Tipo.CARNE.toString());
            List<Churrasco> listChurrasco =  calculaPeso(aux, Integer.valueOf(qntHomens.getText().toString()), Integer.valueOf(qntMulheres.getText().toString()), Integer.valueOf(qntCriancas.getText().toString()));
            itens.add(churrascoAux);
            for (Churrasco c : listChurrasco) {
                if(c.getAtivo()){
                    itens.add(c);
                }
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

    public List<Churrasco> calculaPeso(List<Churrasco> listChurrasco, int qntHomem, int qntMulher, int qntCrianca) {

        /*
        * Gramas de carne dividido por peso 10
        *
        * Homem: 400g de carne por pessoa
        * Mulher: 300g carne por pessoa
        * Crianças: 250g carne por pessoa
        */
        Double gramasCarneHomem = 40.0;
        Double gramasCarneMulher = 30.0;
        Double gramasCarneCrianca = 25.0;

        Double pesoCarneBovina = 3.0;
        Double pesoCarneSuina = 2.0;
        Double pesoCoracao = 1.0;
        Double pesoFrango = 2.0;
        Double pesoLinguica = 2.0;


        int quantidadeCarneSelecionada = 5;
        Double quantidadePeso = 0.0;
        Double pesoDividido = 0.0;

        for (Churrasco churrasco : listChurrasco) {

            if ("Carne Bovina".equals(churrasco.getItem()) && churrasco.getAtivo() == false) {
                quantidadeCarneSelecionada -= 1;
                quantidadePeso += 3.0;
                pesoCarneBovina = 0.0;
            } else if ("Carne Suína".equals(churrasco.getItem()) && churrasco.getAtivo() == false) {
                quantidadeCarneSelecionada -= 1;
                quantidadePeso += 2.0;
                pesoCarneSuina = 0.0;
            } else if ("Coração".equals(churrasco.getItem()) && churrasco.getAtivo() == false) {
                quantidadeCarneSelecionada -= 1;
                quantidadePeso += 1.0;
                pesoCoracao = 0.0;
            } else if ("Frango".equals(churrasco.getItem()) && churrasco.getAtivo() == false) {
                quantidadeCarneSelecionada -= 1;
                quantidadePeso += 2.0;
                pesoFrango = 0.0;
            } else if ("Linguiça".equals(churrasco.getItem()) && churrasco.getAtivo() == false) {
                quantidadeCarneSelecionada -= 1;
                quantidadePeso += 2.0;
                pesoLinguica = 0.0;
            }
        }

        if (quantidadeCarneSelecionada < 5) {

            pesoDividido = quantidadePeso / quantidadeCarneSelecionada;

            for (Churrasco churrasco : listChurrasco) {
                if ("Carne Bovina".equals(churrasco.getItem()) && churrasco.getAtivo()) {
                    pesoCarneBovina += pesoDividido;
                } else if ("Carne Suína".equals(churrasco.getItem()) && churrasco.getAtivo()) {
                    pesoCarneSuina += pesoDividido;
                } else if ("Coração".equals(churrasco.getItem()) && churrasco.getAtivo()) {
                    pesoCoracao += pesoDividido;
                } else if ("Frango".equals(churrasco.getItem()) && churrasco.getAtivo()) {
                    pesoFrango += pesoDividido;
                } else if ("Linguiça".equals(churrasco.getItem()) && churrasco.getAtivo()) {
                    pesoLinguica += pesoDividido;
                }
            }
        }

        // HOMEM
        Double gramasHomemCarneBovina = gramasCarneHomem * pesoCarneBovina;
        Double gramasHomemCarneSuina = gramasCarneHomem * pesoCarneSuina;
        Double gramasHomemCoracao = gramasCarneHomem * pesoCoracao;
        Double gramasHomemFrango = gramasCarneHomem * pesoFrango;
        Double gramasHomemLinguica = gramasCarneHomem * pesoLinguica;

        // MULHER
        Double gramasMulherCarneBovina = gramasCarneMulher * pesoCarneBovina;
        Double gramasMulherCarneSuina = gramasCarneMulher *  pesoCarneSuina;
        Double gramasMulherCoracao = gramasCarneMulher *  pesoCoracao;
        Double gramasMulherFrango = gramasCarneMulher * pesoFrango;
        Double gramasMulherLinguica = gramasCarneMulher * pesoLinguica;

        // CRIANÇA
        Double gramasCriancaCarneBovina = gramasCarneCrianca * pesoCarneBovina;
        Double gramasCriancaCarneSuina = gramasCarneCrianca * pesoCarneSuina;
        Double gramasCriancaCoracao = gramasCarneCrianca * pesoCoracao;
        Double gramasCriancaFrango = gramasCarneCrianca * pesoFrango;
        Double gramasCriancaLinguica = gramasCarneCrianca * pesoLinguica;


        Double quantidadeCarneBovina = (qntHomem * gramasHomemCarneBovina + qntMulher * gramasMulherCarneBovina + qntCrianca * gramasCriancaCarneBovina);
        Double quantidadeCarneSuina = (qntHomem * gramasHomemCarneSuina + qntMulher * gramasMulherCarneSuina + qntCrianca * gramasCriancaCarneSuina);
        Double quantidadeCoracao = (qntHomem * gramasHomemCoracao + qntMulher * gramasMulherCoracao + qntCrianca * gramasCriancaCoracao);
        Double quantidadeFrango = (qntHomem * gramasHomemFrango + qntMulher * gramasMulherFrango + qntCrianca * gramasCriancaFrango);
        Double quantidadeLinguica = (qntHomem * gramasHomemLinguica + qntMulher * gramasMulherLinguica + qntCrianca * gramasCriancaLinguica);

        for (Churrasco churrasco : listChurrasco) {

            if ("Carne Bovina".equals(churrasco.getItem()) ) {
                churrasco.setResultado(quantidadeCarneBovina.toString());
            } else if ("Carne Suína".equals(churrasco.getItem()) ) {
                churrasco.setResultado(quantidadeCarneSuina.toString());
            } else if ("Coração".equals(churrasco.getItem())) {
                churrasco.setResultado(quantidadeCoracao.toString());
            } else if ("Frango".equals(churrasco.getItem()) ) {
                churrasco.setResultado(quantidadeFrango.toString());
            } else if ("Linguiça".equals(churrasco.getItem()) ) {
                churrasco.setResultado(quantidadeLinguica.toString());
            }
        }

        return listChurrasco;

    }
}

