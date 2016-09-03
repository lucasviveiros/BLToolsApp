package br.com.lampmobile.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.helper.ChurrascoHelper;
import br.com.lampmobile.model.Historico;

public class ChurrascoHistoricoAdapter extends RecyclerView.Adapter {

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    List<Historico> items;
    List<Historico> itemsPendingRemoval = new ArrayList<>();
    int lastInsertedIndex; // so we can add some more items for testing purposes
    Context context;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<Historico, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public ChurrascoHistoricoAdapter(Context context, List<Historico> itenm) {
        this.items = itenm;
        this.context = context;

        // let's generate some items
        lastInsertedIndex = itenm.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoricoViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoricoViewHolder viewHolder = (HistoricoViewHolder) holder;
        final Historico item = items.get(position);

        if (itemsPendingRemoval.contains(item)) {
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
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(items.indexOf(item));
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
    public int getItemCount() {
        return items.size();
    }

    public void pendingRemoval(int position) {
        final Historico item = items.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    Historico h = items.get(items.indexOf(item));
                    remove(items.indexOf(item));

                    ChurrascoHelper helper = new ChurrascoHelper(context);
                    SQLiteDatabase db = helper.getReadableDatabase();
                    helper.apagarHistorico(db, h);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Historico item = items.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Historico item = items.get(position);
        return itemsPendingRemoval.contains(item);
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
//        extends RecyclerView.Adapter<ChurrascoHistoricoAdapter.ViewHolder> {
//    private List<Historico> mDataset;
//    private View view;
//    private int lastPosition = -1;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public FrameLayout container;
//        public TextView titulo;
//        public Button favorito;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public ChurrascoHistoricoAdapter(List<Historico> historico) {
//        mDataset = historico;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ChurrascoHistoricoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                                   int viewType) {
//        // create a new view
//        view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_historico, parent, false);
//        if (parent != null) {
//            parent.removeView(view);
//        }
//        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(view);
//        vh.titulo = (TextView) view.findViewById(R.id.historicoDescricao);
//        vh.favorito = (Button) view.findViewById(R.id.historicoFavorito);
//        vh.container = (FrameLayout) view.findViewById(R.id.historicoFrame);
//        return vh;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        holder.titulo.setText(mDataset.get(position).getTitulo());
//        holder.favorito.setBackground(view.getResources().getDrawable(R.drawable.ic_star_border_black_24px));
//
//        // Here you apply the animation when the view is bound
//        setAnimation(holder.container, position);
//    }
//
//    private void setAnimation(View viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
////        if (position > lastPosition) {
////            Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
////            viewToAnimate.startAnimation(animation);
////            lastPosition = position;
////        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
}