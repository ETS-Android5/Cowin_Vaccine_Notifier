package io.realworld.android.cowinvaccinenotifier.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import io.realworld.android.api.models.Center;
import io.realworld.android.cowinvaccinenotifier.Data.Alert;
import io.realworld.android.cowinvaccinenotifier.Data.Subscription;
import io.realworld.android.cowinvaccinenotifier.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    Context context;
    List<Alert> alerts;
    Activity activity;

    public NotificationAdapter(Context context, List<Alert> alerts, Activity activity) {
        this.context = context;
        this.alerts = alerts;
        this.activity = activity;
    }

    public void setAlertSet(HashMap<String, List<Alert>> alertSet) {
        this.alerts = alerts;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item_layout, parent, false);
        return new NotificationAdapter.NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

            Alert alert = alerts.get(position);
            Log.d("yyyy", position +" "+ alerts.size());
            holder.center.setText(alert.getName());
            holder.address.setText(alert.getAddress());
            holder.date.setText(alert.getDate());
            holder.fee_type.setText(alert.getFeeType());
            holder.age_limit.setText(String.valueOf(alert.getAgeLimit()));
            holder.vaccine.setText(alert.getVaccine());
            holder.capacity.setText(String.valueOf(alert.getCapacity()));
            holder.dose_1.setText(String.valueOf(alert.getDose1()));
            holder.dose_2.setText(String.valueOf(alert.getDose2()));

            holder.delete_alert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog mDialog = new AlertDialog.Builder(activity)
                            .setTitle("Delete")
                            .setMessage("Are you sure want to unsubscribe this task?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", (dialog, which) -> {
                                alerts.remove(alert);
                                Paper.init(context);
                                Paper.book().write("test11", alerts);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

            holder.link.setOnClickListener(v ->{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfregistration.cowin.gov.in/"));
                context.startActivity(browserIntent);
            });
    }

    public <K, V> K getKey(HashMap<K, V> map, V value) {
        for (HashMap.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView center;
        TextView address;
        TextView date;
        TextView fee_type;
        TextView age_limit;
        TextView vaccine;
        TextView capacity;
        ImageView delete_alert;
        TextView dose_1;
        TextView dose_2;
        TextView link;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            center = itemView.findViewById(R.id.center);
            address = itemView.findViewById(R.id.address);
            age_limit = itemView.findViewById(R.id.age_limit);
            vaccine = itemView.findViewById(R.id.vaccine);
            delete_alert = itemView.findViewById(R.id.delete_alert);
            capacity = itemView.findViewById(R.id.capacity);
            date = itemView.findViewById(R.id.date);
            fee_type = itemView.findViewById(R.id.fee_type);
            dose_1 = itemView.findViewById(R.id.avail_dose1);
            dose_2 = itemView.findViewById(R.id.avail_dose2);
            link = itemView.findViewById(R.id.link);
        }
    }
}
