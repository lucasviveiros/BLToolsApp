package br.com.lampmobile.activity.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.lampmobile.R;

public class ConsumoActivity extends AppCompatActivity {

    EditText quilometros;
    EditText litros;

    TextView resultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);

        quilometros = (EditText) findViewById(R.id.consumoQuilometro);
        litros = (EditText) findViewById(R.id.consumoCombustivel);
        resultado = (TextView) findViewById(R.id.consumoResultadoDescricao);
    }

    public void calcular(View view)
    {
        if(quilometros.getText() == null || quilometros.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar Quilômetros rodados!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(litros.getText() == null || litros.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar Litros de combustível!", Toast.LENGTH_SHORT).show();
            return;
        }

        Double total;

        Double q = Double.parseDouble(quilometros.getText().toString());
        Double l = Double.parseDouble(litros.getText().toString());

        total = q / l;
        String res = String.format("%.1f", total);
        resultado.setText("Consumo aproximadamente de "+ res +" Km por litro de combustível");

    }
}
