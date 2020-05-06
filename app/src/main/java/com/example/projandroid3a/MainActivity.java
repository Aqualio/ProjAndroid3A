package com.example.projandroid3a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String BASE_URL = "https://raw.githubusercontent.com/BilboBaguette/ProjAndroid3A/master/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeApiCall();
    }

    private void showList(List<NierCharacter> charaList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // define an adapter
        mAdapter = new ListAdapter(charaList);
        recyclerView.setAdapter(mAdapter);
    }

    private void makeApiCall(){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            NierAPI nierAPI = retrofit.create(NierAPI.class);

            Call<RestNierAPIResponse> call = nierAPI.getNierResponse();
            call.enqueue(new Callback<RestNierAPIResponse>() {
                @Override
                public void onResponse(Call<RestNierAPIResponse> call, Response<RestNierAPIResponse> response) {
                    if(response.isSuccessful() && response.body() != null){
                        List<NierCharacter> charaList = response.body().getResults();
                        showList(charaList);
                    }
                }

                @Override
                public void onFailure(Call<RestNierAPIResponse> call, Throwable t) {
                    showError();
                }
            });
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }
}
