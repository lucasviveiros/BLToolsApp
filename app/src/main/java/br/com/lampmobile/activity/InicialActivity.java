package br.com.lampmobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.lampmobile.R;

public class InicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
    }

    public void navCalculadora(View view) {
        Intent intent = new Intent(this, CalculadoraActivity.class);
        startActivity(intent);
    }

    public void navLeitores(View view) {
        Intent intent = new Intent(this, LeitoresActivity.class);
        startActivity(intent);
    }
}
