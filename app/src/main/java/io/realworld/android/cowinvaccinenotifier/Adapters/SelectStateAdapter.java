package io.realworld.android.cowinvaccinenotifier.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realworld.android.api.models.State;
import io.realworld.android.cowinvaccinenotifier.Activies.DistrictActivity;
import io.realworld.android.cowinvaccinenotifier.R;

public class SelectStateAdapter extends RecyclerView.Adapter<SelectStateAdapter.SelectViewHolder> implements Filterable {

    List<State> states;
    private List<State> stateListFull;
    Context context;

    public void setStates(List<State> states) {
        this.states = states;
        stateListFull = new ArrayList<>(states);
        notifyDataSetChanged();
    }

    public SelectStateAdapter(List<State> states, Context context) {
        this.states = states;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new SelectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {

        holder.state.setText(states.get(position).getStateName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DistrictActivity.class);
            intent.putExtra("statecode", states.get(position).getStateId());
            intent.putExtra("state", states.get(position).getStateName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<State> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(stateListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (State item : stateListFull) {
                    if (item.getStateName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            states.clear();
            states.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class SelectViewHolder extends RecyclerView.ViewHolder {

        TextView state;

        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.item);
        }
    }
}
