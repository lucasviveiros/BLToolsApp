package br.com.lampmobile.activity.calculadora;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.adapter.historico.ConsumoHistoricoAdapter;
import br.com.lampmobile.dialog.ConsumoDialogFragment;
import br.com.lampmobile.helper.ConsumoHelper;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.utils.Utils;

public class ConsumoActivity extends CalculadoraActivity {

    EditText quilometros;
    EditText litros;

    String resultado;

    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);

        quilometros = (EditText) findViewById(R.id.consumoQuilometro);
        litros = (EditText) findViewById(R.id.consumoCombustivel);

        getHistorico();

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.consumoAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void calcular(View view)
    {

        //Fechar teclado
        Utils.fecharTeclado(this);

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
        resultado = "Consumo aproximadamente de "+ res +" Km por litro de combustível";

        ConsumoHelper helper = new ConsumoHelper(this);
        ConsumoDialogFragment dialog = new ConsumoDialogFragment();
        dialog.setResultado("Consumo aproximadamente");
        dialog.setResultadoPrincipal(res + " km/l");
        Date data = new Date();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

        dialog.setHistorico(helper.criarHistorico("Cálculo dia "+formatador.format(data).toString(),
                quilometros.getText().toString(), litros.getText().toString()));
        dialog.show(getSupportFragmentManager(), "consumoDialog");

    }

    @Override
    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public void setDados(String q, String l) {
        quilometros.setText(q);
        litros.setText(l);
    }

    public void limpaCampos() {
        quilometros.setText("");
        litros.setText("");
    }

    @Override
    public void getHistorico() {
        ConsumoHelper helper = new ConsumoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Historico> historicos = helper.getHistoricos(db);

        if (historicos != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.consumoHistorico);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            ConsumoHistoricoAdapter imcHistoricoAdapter = new ConsumoHistoricoAdapter(this, historicos);
            // specify an adapter (see also next example)
            mRecyclerView.setAdapter(imcHistoricoAdapter);

            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();
        }
    }

    @Override
    public int getCorFundoExclusao() {
        return R.color.colorPrimaryConsumo;
    }

    @Override
    public void compartilhar(View view) {

        Intent shareIntent = new Intent();

        Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.icone_calculadora);
        shareIntent.putExtra(Intent.EXTRA_TEXT , resultado.toString() + "\n\nLink : " + "http://goo.gl/mR2d" );
        String url= MediaStore.Images.Media.insertImage(getContentResolver(), bm, "iconeLamp", "description");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}
