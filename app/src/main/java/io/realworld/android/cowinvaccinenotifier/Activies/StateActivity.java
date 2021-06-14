package io.realworld.android.cowinvaccinenotifier.Activies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.api.CowinClient;
import io.realworld.android.api.models.State;
import io.realworld.android.api.models.StatesResponse;
import io.realworld.android.cowinvaccinenotifier.Adapters.SelectStateAdapter;
import io.realworld.android.cowinvaccinenotifier.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    SearchView searchView;
    SelectStateAdapter selectStateAdapter;
    List<State> states = new ArrayList<>();
    private final CowinClient cowinClient = new CowinClient();
    private final String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        getSupportActionBar().hide();

        Init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        selectStateAdapter = new SelectStateAdapter(states, this);
        recyclerView.setAdapter(selectStateAdapter);

        showStates();


    }

    private void showStates() {
        Call<StatesResponse> call = cowinClient.api.getStates(user_agent);
        call.enqueue(new Callback<StatesResponse>() {
            @Override
            public void onResponse(Call<StatesResponse> call, Response<StatesResponse> response) {
                if(response.code() == 200){
                    StatesResponse statesResponse = response.body();
                    states = statesResponse.getStates();
                    Log.d("testt",states.get(0).getStateName());
                    selectStateAdapter.setStates(states);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                }else{

                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<StatesResponse> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network Failure! Please check your internet connection and retry", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void Init() {
        Paper.init(this);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

    }
}