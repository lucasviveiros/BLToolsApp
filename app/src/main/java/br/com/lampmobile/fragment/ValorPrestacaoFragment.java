package br.com.lampmobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.lampmobile.R;
import br.com.lampmobile.dialog.TaxaJurosDialogFragment;
import br.com.lampmobile.utils.Utils;

public class ValorPrestacaoFragment extends Fragment implements View.OnClickListener{

    EditText numeroMeses;
    EditText taxaJuros;
    EditText valorFinanciado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_valor_prestacao, container, false);

        numeroMeses = (EditText) rootView.findViewById(R.id.vlrPrestacaoNumeroMeses);
        taxaJuros = (EditText) rootView.findViewById(R.id.vlrPrestacaoJuros);
        valorFinanciado = (EditText) rootView.findViewById(R.id.vlrPrestacaoValorFinanciado);

        Button calcular = (Button) rootView.findViewById(R.id.vlrPrestacaoCalcular);
        calcular.setOnClickListener(this);

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) rootView.findViewById(R.id.vlrPrestacaoAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }


    @Override
    public void onClick(View view) {

        //Fechar teclado
        Utils.fecharTeclado(super.getActivity());

        if(numeroMeses.getText() == null || numeroMeses.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Quantidade de Meses!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(taxaJuros.getText() == null || taxaJuros.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Taxa de Juros!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(valorFinanciado.getText() == null || valorFinanciado.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Valor Financiado!", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer nmMeses = new Integer(numeroMeses.getText().toString());
        BigDecimal vlrFinanciado = new BigDecimal(valorFinanciado.getText().toString());
        BigDecimal txJuros = new BigDecimal(taxaJuros.getText().toString());
        txJuros = txJuros.divide(new BigDecimal(100));

        // vlrFinanciado * txJuros * (1 + i)n
        // ------------------------------
        // (1 + txJuros)n -1

        BigDecimal valor1 = vlrFinanciado.multiply(txJuros);
        Double valor2 = Math.pow(1+txJuros.doubleValue(), nmMeses);

        Double valor3 = Math.pow(1+txJuros.doubleValue(), nmMeses);
        Double valor4 = valor3 - 1;

        BigDecimal total1 = valor1.multiply(new BigDecimal(valor2));
        BigDecimal total2 = new BigDecimal(valor4);

        BigDecimal resultado = total1.divide(total2, 2, RoundingMode.HALF_UP);

        Log.i("RESULTADO : ", resultado.toString());

        TaxaJurosDialogFragment dialog = new TaxaJurosDialogFragment();
        dialog.setResultado("R$ " + resultado.toString());
        dialog.setTitulo("Valor da prestação");

        dialog.show(getFragmentManager(), "txJurosDialog");
    }
}
