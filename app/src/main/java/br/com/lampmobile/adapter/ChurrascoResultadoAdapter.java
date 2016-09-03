package br.com.lampmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.lampmobile.R;
import br.com.lampmobile.helper.ChurrascoHelper;

public class ChurrascoResultadoAdapter extends ArrayAdapter<ChurrascoHelper.Churrasco> {

    public ChurrascoResultadoAdapter(Context context, List<ChurrascoHelper.Churrasco> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChurrascoHelper.Churrasco churrasco = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_resultado_churrasco, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.churrascoResultadoItem);
        TextView tvHome = (TextView) convertView.findViewById(R.id.churrascoResultadoValor);
        // Populate the data into the template view using the data object
        tvName.setText(churrasco.getItem());
        tvHome.setText(churrasco.getResultado());
        // Return the completed view to render on screen
        return convertView;
    }
}
