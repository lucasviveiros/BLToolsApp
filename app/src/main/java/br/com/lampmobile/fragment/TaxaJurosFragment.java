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

import br.com.lampmobile.R;
import br.com.lampmobile.utils.Utils;

public class TaxaJurosFragment extends Fragment implements View.OnClickListener {

    EditText numeroMeses;
    EditText valorParcelas;
    EditText valorFinanciado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_taxa_juros, container, false);

        numeroMeses = (EditText) rootView.findViewById(R.id.txJurosNumeroMeses);
        valorParcelas = (EditText) rootView.findViewById(R.id.txJurosParcelas);
        valorFinanciado = (EditText) rootView.findViewById(R.id.txJurosValorFinanciamento);

        Button calcular = (Button) rootView.findViewById(R.id.txJurosCalcular);
        calcular.setOnClickListener(this);

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) rootView.findViewById(R.id.txJurosAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        //Fechar teclado
        Utils.fecharTeclado(super.getActivity());

        if(valorFinanciado.getText() == null || valorFinanciado.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Valor Financiado!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(valorParcelas.getText() == null || valorParcelas.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Valor das Parcelas!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(numeroMeses.getText() == null || numeroMeses.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Quantidade de Meses!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
