package br.com.lampmobile.adapter.historico;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.lampmobile.model.Historico;

public abstract class HistoricoAdapter extends RecyclerView.Adapter {

    static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    List<Historico> itens;
    List<Historico> itensPendenteRemocao = new ArrayList<>();
    int posicaoUltimo; // so we can add some more itens for testing purposes
    Context context;
    Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Historico, Runnable> pendingRunnables = new HashMap<>(); // map of itens to pending runnables, so we can cancel a removal if need be

    public HistoricoAdapter(Context context, List<Historico> itens) {
        this.itens = itens;
        this.context = context;

        // let's generate some itens
        posicaoUltimo = itens.size();
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void remove(int position) {
        Historico item = itens.get(position);
        if (itensPendenteRemocao.contains(item)) {
            itensPendenteRemocao.remove(item);
        }
        if (itens.contains(item)) {
            itens.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Historico item = itens.get(position);
        return itensPendenteRemocao.contains(item);
    }

    public abstract void pendingRemoval(int position);
}
