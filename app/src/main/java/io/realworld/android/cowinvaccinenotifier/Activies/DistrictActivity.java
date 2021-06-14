package io.realworld.android.cowinvaccinenotifier.Activies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.realworld.android.api.CowinClient;
import io.realworld.android.api.models.District;
import io.realworld.android.api.models.DistrictsResponse;
import io.realworld.android.cowinvaccinenotifier.Adapters.SelectDistrictAdapter;
import io.realworld.android.cowinvaccinenotifier.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistrictActivity extends AppCompatActivity {

    String state;
    long statecode;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView stateName;
    SelectDistrictAdapter selectDistrictAdapter;
    List<District> districts = new ArrayList<>();
    private final CowinClient cowinClient = new CowinClient();
    private final String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        getSupportActionBar().hide();

        statecode = getIntent().getExtras().getLong("statecode");
        Log.e("this", String.valueOf(statecode));
        Init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        selectDistrictAdapter = new SelectDistrictAdapter(districts, this, state);
        recyclerView.setAdapter(selectDistrictAdapter);

        showDistricts();
    }

    private void showDistricts() {
        Call<DistrictsResponse> call = cowinClient.api.getDistricts(statecode, user_agent);
        call.enqueue(new Callback<DistrictsResponse>() {
            @Override
            public void onResponse(Call<DistrictsResponse> call, Response<DistrictsResponse> response) {
                if(response.code() == 200) {
                    DistrictsResponse districtsResponse = response.body();
                    districts = districtsResponse.getDistricts();
                    Log.e("testt", districts.get(0).getDistrictName());
                    selectDistrictAdapter.setDistricts(districts);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DistrictsResponse> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
            }
        });
    }

    private void Init() {
        Paper.init(this);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        stateName = findViewById(R.id.place_name_district);
        searchView = findViewById(R.id.searchView);

        state = getIntent().getExtras().getString("state");
        stateName.setText(state);
    }

}