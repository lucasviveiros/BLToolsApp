package br.com.lampmobile.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.lampmobile.R;

public class HistoricoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titleTextView;
    private Button historicoFavorito;
    private Button undoButton;

    private static ClickListener clickListener;

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(getAdapterPosition(), v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        HistoricoViewHolder.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public HistoricoViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_historico, parent, false));
        itemView.setOnClickListener(this);

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
