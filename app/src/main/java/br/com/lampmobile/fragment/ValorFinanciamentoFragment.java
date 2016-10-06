package br.com.lampmobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigDecimal;

import br.com.lampmobile.R;
import br.com.lampmobile.utils.Utils;

public class ValorFinanciamentoFragment extends Fragment implements View.OnClickListener{

    EditText numeroMeses;
    EditText taxaJuros;
    EditText valorParcela;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_valor_financiamento, container, false);

        numeroMeses = (EditText) rootView.findViewById(R.id.vlrFinanciamentoNumeroMeses);
        taxaJuros = (EditText) rootView.findViewById(R.id.vlrFinanciamentoJuros);
        valorParcela = (EditText) rootView.findViewById(R.id.vlrFinanciamentoValorParcelas);

        Button calcular = (Button) rootView.findViewById(R.id.vlrFinanciamentoCalcular);
        calcular.setOnClickListener(this);

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) rootView.findViewById(R.id.vlrFinanciamentoAdView);
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
        if(valorParcela.getText() == null || valorParcela.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Valor das Parcelas!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Valor Financiado= 1-(1+ Taxa de Juros Mensal) elevado a (menos) - Nº de Meses (divide) por Taxa de Juros Mensal (multiplica) Valor da Prestação

        Integer nmMeses = new Integer(numeroMeses.toString());
        BigDecimal vlrParcela = new BigDecimal(valorParcela.toString());
        BigDecimal txJuros = new BigDecimal(taxaJuros.toString());


        txJuros = txJuros.divide(new BigDecimal(100));



//        Math.pow()



//        BigDecimal q0 = (1 - (txJuros.add(BigDecimal.valueOf(1))) - nmMeses) / txJuros * vlrParcela;

//        q0= 1-(1+j) -n / j *p
    }
}
