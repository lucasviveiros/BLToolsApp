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
import br.com.lampmobile.adapter.historico.CombustivelHistoricoAdapter;
import br.com.lampmobile.dialog.CombustivelDialogFragment;
import br.com.lampmobile.helper.CombustivelHelper;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.utils.Utils;

public class CombustivelActivity extends CalculadoraActivity {

    EditText gasolina;
    EditText alcool;
    String resultado;
    RecyclerView mRecyclerView;
    Double gasolinaConverter;
    Double etanolConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combustivel);

        gasolina = (EditText) findViewById(R.id.combustivelGasolina);
        alcool = (EditText) findViewById(R.id.combustivelAlcool);

        getHistorico();

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.combustivelAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void calcular(View view)
    {
        //Fechar teclado
        Utils.fecharTeclado(this);

        if(gasolina.getText() == null || gasolina.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar valor da Gasolina!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(alcool.getText() == null || alcool.getText().toString().isEmpty()){
            Toast.makeText(getApplication(), "Favor informar valor da Álcool!", Toast.LENGTH_SHORT).show();
            return;
        }

        Double total;

        gasolinaConverter = Double.parseDouble(gasolina.getText().toString());
        etanolConverter = Double.parseDouble(alcool.getText().toString());

        total = etanolConverter / gasolinaConverter;

        String res = "";
        if(total > 0.7)
        {
            resultado = "Abasteça com gasolina";
            res = "GASOLINA";
        }else
        {
            resultado = "Abasteça com álcool";
            res = "ÁLCOOL";
        }

        CombustivelHelper helper = new CombustivelHelper(this);
        CombustivelDialogFragment dialog = new CombustivelDialogFragment();
        dialog.setResultado(resultado);
        dialog.setResultadoPrincipal(res);
        Date data = new Date();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");

        dialog.setHistorico(helper.criarHistorico("Cálculo dia "+formatador.format(data).toString(),
                gasolina.getText().toString(), alcool.getText().toString()));
        dialog.show(getSupportFragmentManager(), "consumoDialog");
    }

    @Override
    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    @Override
    public void getHistorico() {
        CombustivelHelper helper = new CombustivelHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Historico> historicos = helper.getHistoricos(db);

        if (historicos != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.combustivelHistorico);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            CombustivelHistoricoAdapter combustivelHistoricoAdapter = new CombustivelHistoricoAdapter(this, historicos);
            // specify an adapter (see also next example)
            mRecyclerView.setAdapter(combustivelHistoricoAdapter);

            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();
        }
    }

    @Override
    public int getCorFundoExclusao() {
        return R.color.colorPrimaryCombustivel;
    }

    @Override
    public void compartilhar(View view) {
        Intent shareIntent = new Intent();

        Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.icone_calculadora);
        shareIntent.putExtra(Intent.EXTRA_TEXT ,"Gasolina: "+ gasolinaConverter.toString() + "\nEtanol: "+ etanolConverter.toString() +"\n" + resultado.toString() + "\n\nLink : " + "http://goo.gl/mR2d" );
        String url= MediaStore.Images.Media.insertImage(getContentResolver(), bm, "iconeLamp", "description");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    public void setDados(String g, String e) {
        gasolina.setText(g);
        alcool.setText(e);
    }


    public void limpaCampos(){
        gasolina.setText("");
        alcool.setText("");
    }

}
