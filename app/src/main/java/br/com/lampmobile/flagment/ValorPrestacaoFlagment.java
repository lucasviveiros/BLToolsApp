package br.com.lampmobile.flagment;

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

import br.com.lampmobile.R;
import br.com.lampmobile.utils.Utils;

public class ValorPrestacaoFlagment extends Fragment implements View.OnClickListener{

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
    }
}
