package br.com.lampmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import br.com.lampmobile.R;
import br.com.lampmobile.model.Churrasco;

public class ChurrascoResultadoAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<Churrasco> mData = new ArrayList();
    private LayoutInflater mInflater;

    private TreeSet mSeparatorsSet = new TreeSet();

    public ChurrascoResultadoAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Churrasco item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final Churrasco item) {
        mData.add(item);
        // save separator position
        mSeparatorsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Churrasco getItem(int position) {
        return (Churrasco) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.adapter_resultado_churrasco, null);
                    holder.item = (TextView) convertView.findViewById(R.id.churrascoResultadoItem);
                    holder.valor = (TextView) convertView.findViewById(R.id.churrascoResultadoValor);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.adapter_separador_churrasco, null);
                    holder.item = (TextView) convertView.findViewById(R.id.churrascoSeparador);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item.setText(((Churrasco) mData.get(position)).getItem());
        if (holder.valor != null) {
            holder.valor.setText(((Churrasco) mData.get(position)).getResultado());
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView item;
        public TextView valor;
    }
}
