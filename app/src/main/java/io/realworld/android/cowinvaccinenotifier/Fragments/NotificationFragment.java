package io.realworld.android.cowinvaccinenotifier.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.api.models.Center;
import io.realworld.android.cowinvaccinenotifier.Activies.MainActivity;
import io.realworld.android.cowinvaccinenotifier.Activies.StateActivity;
import io.realworld.android.cowinvaccinenotifier.Adapters.HomeAdapter;
import io.realworld.android.cowinvaccinenotifier.Adapters.NotificationAdapter;
import io.realworld.android.cowinvaccinenotifier.Data.Alert;
import io.realworld.android.cowinvaccinenotifier.Data.Subscription;
import io.realworld.android.cowinvaccinenotifier.R;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    TextView textView;
    private FloatingActionButton fab;
    private FloatingActionButton fab_delete;
//    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        Init(root);
//        activity = (MainActivity) requireActivity();
//        activity.mBottomAppBar.bringToFront();
//        activity.fab.bringToFront();

//        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                fab.setOnClickListener(null);
//                activity.mNavController.navigate(R.id.action_notificationFragment_to_homeFragment);
//            }
//        });
//
//        fab = activity.fab;
//        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), StateActivity.class);
                startActivity(intent);
            }
        });

        List<Alert> alerts = Paper.book().read("test11", new ArrayList<>());

        if( alerts.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            notificationAdapter = new NotificationAdapter(getContext(), alerts, getActivity());
            recyclerView.setAdapter(notificationAdapter);
        }
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_LONG).show();


                if( alerts.size() > 0) {

                    AlertDialog mDialog = new AlertDialog.Builder(requireContext())
                            .setTitle("Delete All")
                            .setMessage("Are you sure want to all of this notifications?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", (dialog, which) -> {
                                Paper.book().delete("test11");
                                recyclerView.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    Toast.makeText(requireContext(), "There is no notifications in alert box", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        activity.mBottomAppBar.performShow();
//        activity.mBottomAppBar.setVisibility(View.VISIBLE);
//        activity.mBottomAppBar.bringToFront();
//        activity.fab.show();
//        activity.fab.bringToFront();
//    }
//
//    @Override
//    public void onPause() {
//        fab.setEnabled(true);
//        super.onPause();
//    }

    private void Init(View view) {
        Paper.init(view.getContext());
        recyclerView = view.findViewById(R.id.alert_recycler_view);
        textView = view.findViewById(R.id.no_alerts);
        fab = view.findViewById(R.id.fab_alert);
        fab_delete = view.findViewById(R.id.delete_all);
    }
}