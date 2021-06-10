package io.realworld.android.cowinvaccinenotifier.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.realworld.android.api.models.District;
import io.realworld.android.cowinvaccinenotifier.Activies.OthersActivity;
import io.realworld.android.cowinvaccinenotifier.R;

public class SelectDistrictAdapter extends RecyclerView.Adapter<SelectDistrictAdapter.SelectViewHolder>{

    List<District> districts;
    Context context;
    String state;

    public void setDistricts(List<District> districts) {
        this.districts = districts;
        notifyDataSetChanged();
    }

    public SelectDistrictAdapter(List<District> districts, Context context, String state) {
        this.districts = districts;
        this.context = context;
        this.state = state;
    }

    @NonNull
    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new SelectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, int position) {

        holder.district.setText(districts.get(position).getDistrictName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OthersActivity.class);
            intent.putExtra("statecode", districts.get(position).getDistrictId());
            intent.putExtra("state", state);
            intent.putExtra("district", districts.get(position).getDistrictName());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return districts.size();
    }

    public class SelectViewHolder extends RecyclerView.ViewHolder {

        TextView district;

        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);
            district = itemView.findViewById(R.id.item);
        }
    }
}
