package br.com.bltoolsapplication.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.bltoolsapplication.R;

public class ImcActivity extends AppCompatActivity {

    EditText alturaMetros;
    EditText alturaCentimetro;
    EditText peso;
    TextView resultado;
    TextView resultadoDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        alturaMetros = (EditText) findViewById(R.id.imcAlturaMetros);
        alturaCentimetro = (EditText) findViewById(R.id.imcAlturaCentimetro);
        peso = (EditText) findViewById(R.id.imcPeso);
        resultado = (TextView) findViewById(R.id.imcResultado);
        resultadoDescricao = (TextView) findViewById(R.id.imcResultadoDescricao);


        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void Calcular(View view) {
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
        resultado.setText(res);

        if(imc < 18.5){
            resultadoDescricao.setText("Abaixo do peso");
        }else if(imc > 18.5 && imc < 24.9){
            resultadoDescricao.setText("Peso normal");
        }else if(imc > 25 && imc < 29.9){
            resultadoDescricao.setText("Sobrepeso (acima do peso desejado)");
        }else if(imc > 30 ){
            resultadoDescricao.setText("Obesidade");
        }
    }
}
