package br.com.lampmobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.lampmobile.R;
import br.com.lampmobile.activity.calculadora.ChurrascoActivity;
import br.com.lampmobile.activity.calculadora.ConsumoActivity;
import br.com.lampmobile.activity.calculadora.CombustivelActivity;
import br.com.lampmobile.activity.calculadora.ImcActivity;
import br.com.lampmobile.activity.calculadora.TxJurosActivity;

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
        Intent intent = new Intent(this, CombustivelActivity.class);
        startActivity(intent);
    }

    public void navConsumo(View view) {
        Intent intent = new Intent(this, ConsumoActivity.class);
        startActivity(intent);
    }

    public void navTxJuros(View view) {
        Intent intent = new Intent(this, TxJurosActivity.class);
        startActivity(intent);
    }
}
