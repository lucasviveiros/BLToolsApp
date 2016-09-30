package br.com.lampmobile.activity.calculadora;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.lampmobile.R;
import br.com.lampmobile.adapter.PagerAdapter;

public class TxJurosActivity extends CalculadoraActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getSupportActionBar().setElevation(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_juros);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Prestacao"));
        tabLayout.addTab(tabLayout.newTab().setText("Financiamento"));
        tabLayout.addTab(tabLayout.newTab().setText("Juros"));
        tabLayout.addTab(tabLayout.newTab().setText("Meses"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void calcular(View view) {

    }

    @Override
    public RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    public void getHistorico() {
    }

    @Override
    public int getCorFundoExclusao() {
        return 0;
    }

    @Override
    public void compartilhar(View view) {

    }
}
