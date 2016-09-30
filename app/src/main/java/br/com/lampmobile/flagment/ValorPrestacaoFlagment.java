package br.com.lampmobile.flagment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.lampmobile.R;

public class ValorPrestacaoFlagment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_valor_prestacao, container, false);

        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) rootView.findViewById(R.id.vlrPrestacaoAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }
}
