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


public class MesesFragment extends Fragment implements View.OnClickListener{

    EditText valorParcelas;
    EditText taxaJuros;
    EditText valorFinanciado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meses, container, false);

        valorParcelas = (EditText) rootView.findViewById(R.id.mesesValorParcelas);
        taxaJuros = (EditText) rootView.findViewById(R.id.mesesJuros);
        valorFinanciado = (EditText) rootView.findViewById(R.id.mesesValorFinanciamento);

        Button calcular = (Button) rootView.findViewById(R.id.mesesCalcular);
        calcular.setOnClickListener(this);

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) rootView.findViewById(R.id.mesesAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        //Fechar teclado
        Utils.fecharTeclado(super.getActivity());

        if(taxaJuros.getText() == null || taxaJuros.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Taxa de Juros!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(valorParcelas.getText() == null || valorParcelas.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Valor Parcelas!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(valorFinanciado.getText() == null || valorFinanciado.getText().toString().isEmpty()){
            Toast.makeText(super.getContext(), "Favor informar Valor Financiado!", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal vlrFinanciado = new BigDecimal(valorFinanciado.toString());
        BigDecimal vlrParcela = new BigDecimal(valorParcelas.toString());
        BigDecimal txJuros = new BigDecimal(taxaJuros.toString());

        txJuros = txJuros.divide(new BigDecimal(100));

    }
}
