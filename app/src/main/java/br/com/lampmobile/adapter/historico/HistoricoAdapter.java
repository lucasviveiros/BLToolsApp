package br.com.lampmobile.adapter.historico;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.model.Historico;
import br.com.lampmobile.viewHolder.HistoricoViewHolder;

public abstract class HistoricoAdapter extends RecyclerView.Adapter {

    static final int PENDING_REMOVAL_TIMEOUT = 2000; // 2segundos

    List<Historico> itens;
    List<Historico> itensPendenteExclusao = new ArrayList<>();
    Context context;
    Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Historico, Runnable> pendingRunnables = new HashMap<>(); // map of itens to pending runnables, so we can cancel a removal if need be

    public HistoricoAdapter(Context context, List<Historico> itens) {
        this.itens = itens;
        this.context = context;
    }

    public abstract int getCorFundoRemocao();
    public abstract void itemPendenteExclusao(int position);
    public abstract void itemSelecionadoFavorito(int position);

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void remover(int position) {
        Historico item = itens.get(position);
        if (itensPendenteExclusao.contains(item)) {
            itensPendenteExclusao.remove(item);
        }
        if (itens.contains(item)) {
            itens.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void alterar(int position) {
        Historico historico = itens.get(position);
        notifyItemChanged(position, historico);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoricoViewHolder viewHolder = (HistoricoViewHolder) holder;
        final Historico item = itens.get(position);

        if (itensPendenteExclusao.contains(item)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(getCorFundoRemocao()));
            viewHolder.getTitleTextView().setVisibility(View.GONE);
            viewHolder.getHistoricoFavorito().setVisibility(View.GONE);
            viewHolder.getUndoButton().setVisibility(View.VISIBLE);
            viewHolder.getUndoButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itensPendenteExclusao.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(itens.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
//            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.getTitleTextView().setVisibility(View.VISIBLE);
            viewHolder.getTitleTextView().setText(item.getTitulo());
            viewHolder.getHistoricoFavorito().setVisibility(View.VISIBLE);
            if (item.getFavorito()) {
                viewHolder.getHistoricoFavorito().setBackground(context.getResources().getDrawable(R.drawable.ic_star_yellow_24px));
            } else {
                viewHolder.getHistoricoFavorito().setBackground(context.getResources().getDrawable(R.drawable.ic_star_border_black_24px));
            }
            viewHolder.getHistoricoFavorito().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemSelecionadoFavorito(itens.indexOf(item));
                }
            });


            viewHolder.getUndoButton().setVisibility(View.GONE);
            viewHolder.getUndoButton().setOnClickListener(null);
        }
    }

    public boolean isPententeExclusao(int position) {
        Historico item = itens.get(position);
        return itensPendenteExclusao.contains(item);
    }
}
