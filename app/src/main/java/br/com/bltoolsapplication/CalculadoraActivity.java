package br.com.bltoolsapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.bltoolsapplication.calculadora.ChurrascoActivity;
import br.com.bltoolsapplication.calculadora.ConsumoActivity;
import br.com.bltoolsapplication.calculadora.GasolinaActivity;
import br.com.bltoolsapplication.calculadora.ImcActivity;

public class CalculadoraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
    }

    public void navImc(View view) {
        Intent intent = new Intent(this, ImcActivity.class);
        startActivity(intent);
    }

    public void navChurrasco(View view) {
        Intent intent = new Intent(this, ChurrascoActivity.class);
        startActivity(intent);
    }

    public void navGasolina(View view) {
        Intent intent = new Intent(this, GasolinaActivity.class);
        startActivity(intent);
    }

    public void navConsumo(View view) {
        Intent intent = new Intent(this, ConsumoActivity.class);
        startActivity(intent);
    }
}
