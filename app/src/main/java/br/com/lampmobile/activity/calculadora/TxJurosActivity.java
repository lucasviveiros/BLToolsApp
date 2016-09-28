package br.com.lampmobile.activity.calculadora;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.lampmobile.R;

public class TxJurosActivity extends CalculadoraActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setElevation(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_juros);

//        alturaMetros = (EditText) findViewById(R.id.imcAlturaMetros);
//        alturaCentimetro = (EditText) findViewById(R.id.imcAlturaCentimetro);
//        peso = (EditText) findViewById(R.id.imcPeso);
        // RECUPERA HISTÓRICO
        getHistorico();
        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.txJurosAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void calcular(View view) {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    public void getHistorico() {

    }

    @Override
    public int getCorFundoExclusao() {
        return 0;
    }

    @Override
    public void compartilhar(View view) {

    }
}
