package io.realworld.android.cowinvaccinenotifier.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.cowinvaccinenotifier.Adapters.HomeAdapter;
import io.realworld.android.cowinvaccinenotifier.Data.Subscription;
import io.realworld.android.cowinvaccinenotifier.R;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Init(root);
        List<Subscription> subscriptions = Paper.book().read("subscription", new ArrayList<>());

        if( subscriptions.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            homeAdapter = new HomeAdapter(getContext(), subscriptions, getActivity());
            recyclerView.setAdapter(homeAdapter);
        }
        return root;
    }

    private void Init(View view) {
        Paper.init(view.getContext());
        recyclerView = view.findViewById(R.id.home_recycler_view);
        textView = view.findViewById(R.id.no_subs);
    }
}