package br.com.lampmobile.adapter.historico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Historico;

public class ChurrascoHistoricoAdapter extends HistoricoAdapter {

    public ChurrascoHistoricoAdapter(Context context, List<Historico> itens) {
        super(context, itens);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoricoViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoricoViewHolder viewHolder = (HistoricoViewHolder) holder;
        final Historico item = itens.get(position);

        if (itensPendenteRemocao.contains(item)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryChurrasco));
            viewHolder.titleTextView.setVisibility(View.GONE);
            viewHolder.historicoFavorito.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itensPendenteRemocao.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(itens.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
//            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.titleTextView.setVisibility(View.VISIBLE);
            viewHolder.titleTextView.setText(item.getTitulo());
            viewHolder.historicoFavorito.setVisibility(View.VISIBLE);
            viewHolder.historicoFavorito.setBackground(context.getResources().getDrawable(R.drawable.ic_star_border_black_24px));
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }

    @Override
    public void pendingRemoval(int position) {
        final Historico item = itens.get(position);
        if (!itensPendenteRemocao.contains(item)) {
            itensPendenteRemocao.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    Historico h = itens.get(itens.indexOf(item));
                    remove(itens.indexOf(item));

                    ChurrascoHelper helper = new ChurrascoHelper(context);
                    SQLiteDatabase db = helper.getReadableDatabase();
                    helper.apagarHistorico(db, h);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    static class HistoricoViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        Button historicoFavorito;
        Button undoButton;

        public HistoricoViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_historico, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.historicoDescricao);
            historicoFavorito = (Button) itemView.findViewById(R.id.historicoFavorito);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }

    }
}