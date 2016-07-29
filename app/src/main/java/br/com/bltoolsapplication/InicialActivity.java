package br.com.bltoolsapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
}
