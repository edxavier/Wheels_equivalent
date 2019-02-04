package com.edxavier.wheels_equivalent;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Eder Xavier Rojas on 12/07/2016.
 */
public class AdapterEq extends RecyclerView.Adapter<AdapterEq.ViewHolder> {
    ArrayList<Equivalence> equivalences = null;
    Context context;

    public AdapterEq(ArrayList<Equivalence> equivalences, Context context) {
        this.equivalences = equivalences;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView suggestedTire;
        TextView diffTire;
        TextView speedTire;
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            suggestedTire =  itemView.findViewById(R.id.txtSuggested_tire);
            diffTire =  itemView.findViewById(R.id.txtDifference);
            speedTire =  itemView.findViewById(R.id.txtSpeed);
            container = itemView.findViewById(R.id.row_container);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_equivalences, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Equivalence eq = equivalences.get(position);
        if( (position + 1) % 2 !=0) {
            holder.container.setBackgroundColor(ContextCompat.getColor(context, R.color.md_grey_50));
        }
        holder.suggestedTire.setText(String.format(Locale.getDefault(), "%.0f/%.0f R%.0f", eq.width, eq.perfil, eq.rin));
        holder.diffTire.setText(String.format(Locale.getDefault(), "%.1f", eq.diference));
        holder.speedTire.setText(String.format(Locale.getDefault(), "%.1f km/h", eq.speed));
    }


    @Override
    public int getItemCount() {
        if (equivalences != null) {
            return equivalences.size();
        } else {
            return 0;
        }
    }

}
