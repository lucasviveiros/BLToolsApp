package br.com.lampmobile.adapter.historico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.viewHolder.HistoricoViewHolder;

public class ChurrascoHistoricoAdapter extends HistoricoAdapter {

    public ChurrascoHistoricoAdapter(Context context, List<Historico> itens) {
        super(context, itens);
    }

    @Override
    public int getCorFundoRemocao() {
        return R.color.colorPrimaryChurrasco;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoricoViewHolder(parent);
    }

    @Override
    public void itemPendenteExclusao(int position) {
        final Historico item = itens.get(position);
        if (!itensPendenteExclusao.contains(item)) {
            itensPendenteExclusao.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remover the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    Historico h = itens.get(itens.indexOf(item));
                    remover(itens.indexOf(item));

                    ChurrascoHelper helper = new ChurrascoHelper(context);
                    SQLiteDatabase db = helper.getReadableDatabase();
                    helper.apagarHistorico(db, h);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    @Override
    public void itemSelecionadoFavorito(int position) {
        final Historico historico = itens.get(position);
        ChurrascoHelper helper = new ChurrascoHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        if (historico.getFavorito()) {
            historico.setFavorito(Boolean.FALSE);
        } else {
            historico.setFavorito(Boolean.TRUE);
        }

        helper.atualizarFavoritoHistorico(db, historico);
        itens.get(position).setFavorito(historico.getFavorito());
        alterar(position);
    }

}