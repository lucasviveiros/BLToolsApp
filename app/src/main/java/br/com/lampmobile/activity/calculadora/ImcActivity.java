package br.com.lampmobile.activity.calculadora;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.lampmobile.R;
import br.com.lampmobile.dialog.ImcDialogFragment;
import br.com.lampmobile.utils.Utils;

public class ImcActivity extends AppCompatActivity {

    EditText alturaMetros;
    EditText alturaCentimetro;
    EditText peso;
    String resultado;

    //TextView resultado;
    //TextView resultadoDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        alturaMetros = (EditText) findViewById(R.id.imcAlturaMetros);
        alturaCentimetro = (EditText) findViewById(R.id.imcAlturaCentimetro);
        peso = (EditText) findViewById(R.id.imcPeso);
        //resultado = (TextView) findViewById(R.id.imcResultado);
        //resultadoDescricao = (TextView) findViewById(R.id.imcResultadoDescricao);


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

        ImcDialogFragment dialog = new ImcDialogFragment();
        dialog.setResultado(resultado);
        dialog.show(getSupportFragmentManager(), "imcDialog");

    }
}
