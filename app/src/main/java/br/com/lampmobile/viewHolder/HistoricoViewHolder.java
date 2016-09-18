package br.com.lampmobile.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.lampmobile.R;

public class HistoricoViewHolder extends RecyclerView.ViewHolder {

    private TextView titleTextView;
    private Button historicoFavorito;
    private Button undoButton;

    public HistoricoViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_historico, parent, false));
        titleTextView = (TextView) itemView.findViewById(R.id.historicoDescricao);
        historicoFavorito = (Button) itemView.findViewById(R.id.historicoFavorito);
        undoButton = (Button) itemView.findViewById(R.id.undo_button);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public Button getHistoricoFavorito() {
        return historicoFavorito;
    }

    public void setHistoricoFavorito(Button historicoFavorito) {
        this.historicoFavorito = historicoFavorito;
    }

    public Button getUndoButton() {
        return undoButton;
    }

    public void setUndoButton(Button undoButton) {
        this.undoButton = undoButton;
    }
}
