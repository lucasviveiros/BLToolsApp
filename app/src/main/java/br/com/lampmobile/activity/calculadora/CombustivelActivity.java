package br.com.lampmobile.activity.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.lampmobile.R;

public class CombustivelActivity extends AppCompatActivity {

    EditText gasolina;
    EditText alcool;
    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combustivel);

        gasolina = (EditText) findViewById(R.id.combustivelGasolina);
        alcool = (EditText) findViewById(R.id.combustivelAlcool);
        resultado = (TextView) findViewById(R.id.combustivelResultadoDescricao);
    }

    public void calcular(View view)
    {
        if(gasolina.getText() == null || gasolina.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar valor da Gasolina!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(alcool.getText() == null || alcool.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar valor da Álcool!", Toast.LENGTH_SHORT).show();
            return;
        }

        Double total;

        Double g = Double.parseDouble(gasolina.getText().toString());
        Double a = Double.parseDouble(alcool.getText().toString());

        total = a / g;

        if(total > 0.7)
        {
            resultado.setText("ABASTEÇA COM GASOLINA");
        }else
        {
            resultado.setText("ABASTEÇA COM ÁLCOOL");
        }
    }
}
