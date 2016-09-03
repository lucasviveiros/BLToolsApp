package br.com.lampmobile.activity.calculadora;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.lampmobile.R;
import br.com.lampmobile.adapter.ChurrascoHistoricoAdapter;
import br.com.lampmobile.dialog.ChurrascoDialogFragment;
import br.com.lampmobile.dialog.ChurrascoResultadoDialogFragment;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Churrasco;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.utils.Utils;

public class ChurrascoActivity extends CalculadoraActivity {

    EditText qntHomens;
    EditText qntMulheres;
    EditText qntCriancas;
    TextView resultado;
    RecyclerView mRecyclerView;

    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;

    enum Direction {LEFT, RIGHT;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churrasco);

        qntHomens = (EditText) findViewById(R.id.churrascoHomens);
        qntMulheres = (EditText) findViewById(R.id.churrascoMulheres);
        qntCriancas = (EditText) findViewById(R.id.churrascoCriancas);
        resultado = (TextView) findViewById(R.id.churrascoResultado);
        // RECUPERA HISTÓRICO
        getHistorico();
        // INICIALIZA PROPAGANDA
        AdView mAdView = (AdView) findViewById(R.id.churrascoAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment newFragment = new ChurrascoDialogFragment();
        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<ChurrascoHelper.Tipo, List<Churrasco>> churrascos = helper.getItens(db);

        List<Churrasco> itens = churrascos.get(ChurrascoHelper.Tipo.CARNE);
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.BEBIDA));
        itens.addAll(churrascos.get(ChurrascoHelper.Tipo.OUTROS));

        ((ChurrascoDialogFragment) newFragment).criarArrays(itens); //TODO MUDAR PARA ADD TODOS
        newFragment.show(getSupportFragmentManager(), "confChurras");

        return true;
    }

    @Override
    public void calcular(View view) {
        //Fechar teclado
        Utils.fecharTeclado(this);

        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<ChurrascoHelper.Tipo, List<Churrasco>> mapChurrasco = helper.getItensAtivos(db);
        List<Churrasco> aux;
        List<Churrasco> itens = new ArrayList<>();
        Churrasco churrascoAux = new Churrasco();

        int contador = 0;
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.CARNE);
        churrascoAux.setItem(ChurrascoHelper.Tipo.CARNE.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("10 kg");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.CARNE, itens);

        contador = 0;
        itens = new ArrayList<>();
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.ACOMPANHAMENTO);
        churrascoAux = new Churrasco();
        churrascoAux.setItem(ChurrascoHelper.Tipo.ACOMPANHAMENTO.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("10 kg");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.ACOMPANHAMENTO, itens);

        contador = 0;
        itens = new ArrayList<>();
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.BEBIDA);
        churrascoAux = new Churrasco();
        churrascoAux.setItem(ChurrascoHelper.Tipo.BEBIDA.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("5 litros");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.BEBIDA, itens);

        contador = 0;
        itens = new ArrayList<>();
        aux = mapChurrasco.get(ChurrascoHelper.Tipo.OUTROS);
        churrascoAux = new Churrasco();
        churrascoAux.setItem(ChurrascoHelper.Tipo.OUTROS.toString());
        itens.add(churrascoAux);
        itens.addAll(aux);
        for (Churrasco item : itens) {
            if (contador == 0) {
                contador++;
            } else {
                item.setResultado("50 copos");
            }
        }
        mapChurrasco.put(ChurrascoHelper.Tipo.OUTROS, itens);

        ChurrascoResultadoDialogFragment dialog = new ChurrascoResultadoDialogFragment();
        dialog.setMapChurrasco(mapChurrasco);
        dialog.setPessoas(calcularNumeroPessoas());
        dialog.setHistorico(helper.criarHistorico(calcularNumeroPessoas(),
                qntHomens.getText().toString(), qntMulheres.getText().toString(), qntCriancas.getText().toString()));
        dialog.show(getSupportFragmentManager(), "resultChurras");
    }

    private String calcularNumeroPessoas() {
        String resultado;
        int adultos = 0;
        int criancas = 0;
        if (qntHomens.getText() != null && !qntHomens.getText().toString().isEmpty()) {
            adultos = Integer.valueOf(qntHomens.getText().toString());
        }

        if (qntMulheres.getText() != null && !qntMulheres.getText().toString().isEmpty()) {
            adultos += Integer.valueOf(qntMulheres.getText().toString());
        }

        if (qntCriancas.getText() != null && !qntCriancas.getText().toString().isEmpty()) {
            criancas = Integer.valueOf(qntCriancas.getText().toString());
        }

        resultado = ((adultos > 0 ? adultos + " adultos " : ""));
        if (adultos == 0) {
            resultado = (resultado + (criancas > 0 ? criancas + " crianças." : ""));
        } else {
            resultado = (resultado + (criancas > 0 ? "e " + criancas + " crianças." : ""));
        }

        return resultado;
    }

    private void getHistorico() {
        ChurrascoHelper helper = new ChurrascoHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Historico> historicos = helper.getHistoricos(db);

        if (historicos != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.churrascoHistorico);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            ChurrascoHistoricoAdapter mAdapter = new ChurrascoHistoricoAdapter(getApplicationContext(), historicos);
            // specify an adapter (see also next example)
            mRecyclerView.setAdapter(mAdapter);
            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();
        }
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.colorPrimaryChurrasco));
                xMark = ContextCompat.getDrawable(ChurrascoActivity.this, R.drawable.ic_delete_forever_white_24px);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ChurrascoActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ChurrascoHistoricoAdapter testAdapter = (ChurrascoHistoricoAdapter) recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ChurrascoHistoricoAdapter adapter = (ChurrascoHistoricoAdapter) mRecyclerView.getAdapter();
                adapter.pendingRemoval(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.colorPrimaryChurrasco));
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }
}
