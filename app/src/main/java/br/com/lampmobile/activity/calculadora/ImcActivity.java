package br.com.lampmobile.activity.calculadora;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.adapter.historico.ImcHistoricoAdapter;
import br.com.lampmobile.dialog.ImcDialogFragment;
import br.com.lampmobile.helper.ImcHelper;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.utils.Utils;

public class ImcActivity extends CalculadoraActivity {

    EditText alturaMetros;
    EditText alturaCentimetro;
    EditText peso;
    String resultado;
    RecyclerView mRecyclerView;

    //TextView resultado;
    //TextView resultadoDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        alturaMetros = (EditText) findViewById(R.id.imcAlturaMetros);
        alturaCentimetro = (EditText) findViewById(R.id.imcAlturaCentimetro);
        peso = (EditText) findViewById(R.id.imcPeso);
        // RECUPERA HISTÓRICO
        getHistorico();
        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void calcular(View view) {

        //Fechar teclado
        Utils.fecharTeclado(this);

        if(alturaMetros.getText() == null || alturaMetros.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar Altura!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(alturaCentimetro.getText() == null || alturaCentimetro.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar Centímetros!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(peso.getText() == null || peso.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar Peso!", Toast.LENGTH_SHORT).show();
            return;
        }

        Double totalMetros;
        Double total;

        Double aM = Double.parseDouble(alturaMetros.getText().toString());
        Double aC = Double.parseDouble(alturaCentimetro.getText().toString());

        totalMetros = aM * 100;

        total = (aC + totalMetros) / 100;


        Double p = Double.parseDouble(peso.getText().toString());
        Double imc;
        imc = (p)/(total * total);

        String res = String.format("%.2f", imc);

        if(imc < 18.5){
            resultado = res + " - Abaixo do peso";
        }else if(imc > 18.5 && imc < 24.9){
            resultado = res + " - Peso normal";
        }else if(imc > 25 && imc < 29.9){
            resultado = res + " - Sobrepeso (acima do peso desejado)";
        }else if(imc > 30 ){
            resultado = res + " - Obesidade";
        }
        ImcHelper helper = new ImcHelper(this);
        ImcDialogFragment dialog = new ImcDialogFragment();
        dialog.setResultado(resultado);
        dialog.setHistorico(helper.criarHistorico(resultado,
                alturaMetros.getText().toString(), alturaCentimetro.getText().toString(), peso.getText().toString()));
        dialog.show(getSupportFragmentManager(), "imcDialog");




    }

    @Override
    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    @Override
    public void getHistorico() {

        ImcHelper helper = new ImcHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Historico> historicos = helper.getHistoricos(db);

        if (historicos != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.imcHistorico);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            ImcHistoricoAdapter imcHistoricoAdapter = new ImcHistoricoAdapter(this, historicos);
            // specify an adapter (see also next example)
            mRecyclerView.setAdapter(imcHistoricoAdapter);

            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();
        }

    }

    @Override
    public int getCorFundoExclusao() {
        return R.color.colorPrimaryImc;
    }

    public void setDados(String metros, String centimetros, String kilo) {
        alturaMetros.setText(metros);
        alturaCentimetro.setText(centimetros);
        peso.setText(kilo);
    }

}
