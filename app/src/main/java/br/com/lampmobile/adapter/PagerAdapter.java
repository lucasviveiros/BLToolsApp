package br.com.lampmobile.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.lampmobile.fragment.MesesFragment;
import br.com.lampmobile.fragment.TaxaJurosFragment;
import br.com.lampmobile.fragment.ValorFinanciamentoFragment;
import br.com.lampmobile.fragment.ValorPrestacaoFragment;

/**
 * Created by lucas.viveiros on 29/09/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ValorPrestacaoFragment prestacao = new ValorPrestacaoFragment();
                return prestacao;
            case 1:
                ValorFinanciamentoFragment financiamento = new ValorFinanciamentoFragment();
                return financiamento;
            case 2:
                TaxaJurosFragment taxaJuros = new TaxaJurosFragment();
                return taxaJuros;
            case 3:
                MesesFragment meses = new MesesFragment();
                return meses;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}