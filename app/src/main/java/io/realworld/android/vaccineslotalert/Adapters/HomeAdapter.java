package io.realworld.android.vaccineslotalert.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import io.paperdb.Paper;
import io.realworld.android.api.models.Center;
import io.realworld.android.vaccineslotalert.Data.Subscription;
import io.realworld.android.vaccineslotalert.R;

/**
 * Show data as a list in a view recycling manner
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    Context context;
    List<Subscription> subscriptions;
    Activity activity;

    public HomeAdapter(Context context, List<Subscription> subscriptions, Activity activity) {
        this.context = context;
        this.subscriptions = subscriptions;
        this.activity = activity;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscriptions_item_layout, parent, false);
        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Subscription subscription = subscriptions.get(position);

        if(subscription.getPin() == null){
            holder.district.setVisibility(View.VISIBLE);
            holder.state.setPadding(0, 0, 0, 0);
            holder.state.setText(subscription.getState());
            holder.district.setText(subscription.getDistrict());
        } else {
            holder.state.setText(subscription.getPin());
            holder.district.setVisibility(View.GONE);
            holder.state.setPadding(5, 15, 0, 15);
        }
        List<Integer> ages = subscription.getAges();

        if (ages.size() == 0 || ages.size() == 2) {
             holder.age_18.setVisibility(View.VISIBLE);
             holder.age_45.setVisibility(View.VISIBLE);
        } else if (ages.get(0) == 18){
             holder.age_18.setVisibility(View.VISIBLE);
             holder.age_45.setVisibility(View.GONE);
        } else if (ages.get(0) == 45){
             holder.age_18.setVisibility(View.GONE);
             holder.age_45.setVisibility(View.VISIBLE);
        }


        List<Integer> doses = subscription.getDoses();
        if(doses.size() == 0 || doses.size() == 2){
            holder.dose_1.setVisibility(View.VISIBLE);
            holder.dose_2.setVisibility(View.VISIBLE);
        } else if (doses.get(0) == 1){
                holder.dose_2.setVisibility(View.GONE);
                holder.dose_1.setVisibility(View.VISIBLE);
        }else if (doses.get(0) == 2){
                holder.dose_2.setVisibility(View.VISIBLE);
                holder.dose_1.setVisibility(View.GONE);
        }


        holder.delete.setOnClickListener(v -> {
            AlertDialog mDialog = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure! You want to unsubscribe this task?")
                    .setCancelable(false)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        long districtID = subscription.getDistrictId();
                        String pinCode = subscription.getPin();
                        if (districtID != 0L && pinCode == null) {

                            List<Integer> ages1 = subscription.getAges();
                            List<Integer> doses1 = subscription.getDoses();

                            StringBuilder builder = new StringBuilder(String.valueOf(districtID));
                            for (int a = 0; a < ages1.size(); a++) {
                                builder.append(ages1.get(a));
                            }
                            for (int d = 0; d < doses1.size(); d++) {
                                builder.append(doses1.get(d));
                            }
                            String code = builder.toString();

                            subscriptions.remove(subscription);
                            Paper.init(context);
                            Paper.book().write("subscription", subscriptions);
                            HashMap<String, List<Center> > centers = Paper.book().read("test31", new HashMap<>());
                            centers.remove(code);
                            Paper.book().write("test31", centers);
                            notifyDataSetChanged();

                        }  else if (districtID == 0L &&  pinCode!=null) {
                            Log.d("assa", "hhhhhhhh");
                            List<Integer> ages1 = subscription.getAges();
                            List<Integer> doses1 = subscription.getDoses();

                            StringBuilder builder = new StringBuilder(pinCode);
                            for (int a = 0; a < ages1.size(); a++) {
                                builder.append(ages1.get(a));
                            }
                            for (int d = 0; d < doses1.size(); d++) {
                                builder.append(doses1.get(d));
                            }
                            String code = builder.toString();

                            subscriptions.remove(subscription);
                            Paper.init(context);
                            Paper.book().write("subscription", subscriptions);
                            HashMap<String, List<Center> > centers = Paper.book().read("test31", new HashMap<>());
                            centers.remove(code);
                            Paper.book().write("test31", centers);
                            notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        });
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    /**
     * Holding the view for data
     */
    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView state;
        TextView district;
        TextView dose_1, dose_2;
        TextView age_18, age_45;
        ImageView delete;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.txt_state);
            district = itemView.findViewById(R.id.txt_district);
            age_18 = itemView.findViewById(R.id.age_18);
            age_45 = itemView.findViewById(R.id.age_45);
            delete = itemView.findViewById(R.id.delete);
            dose_1 = itemView.findViewById(R.id.dose_1);
            dose_2 = itemView.findViewById(R.id.dose_2);
        }
    }
}
