package com.example.projandroid3a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ListAdapter.onNoteListener {

    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String BASE_URL = "https://raw.githubusercontent.com/BilboBaguette/ProjAndroid3A/master/";
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private ArrayList<NierCharacter> charArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("app_nier", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();

        List<NierCharacter> charaList = getDataFromCache();
        if(charaList != null){
            showList(charaList);
        }else {
            makeApiCall();
        }
        charArrayList = getArrayDataFromCache();
        if(charArrayList == null){
            makeApiCall();
        }
    }

    private List<NierCharacter> getDataFromCache() {
        String jsonNier = sharedPreferences.getString("jsonNierList", null);

        if (jsonNier == null){
            return null;
        }else{
            Type listType = new TypeToken<List<NierCharacter>>(){}.getType();
            return gson.fromJson(jsonNier, listType);
        }
    }

    private ArrayList<NierCharacter> getArrayDataFromCache(){
        String jsonNier = sharedPreferences.getString("jsonNierList", null);

        if (jsonNier == null){
            return null;
        }else{
            Type listType = new TypeToken<ArrayList<NierCharacter>>(){}.getType();
            return gson.fromJson(jsonNier, listType);
        }
    }

    private void showList(List<NierCharacter> charaList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // define an adapter
        mAdapter = new ListAdapter(charaList, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void makeApiCall(){
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
                        saveList(charaList);
                        showList(charaList);
                    }
                }

                @Override
                public void onFailure(Call<RestNierAPIResponse> call, Throwable t) {
                    showError();
                }
            });
    }

    private void saveList(List<NierCharacter> charaList) {
        String jsonString = gson.toJson(charaList);
        sharedPreferences
                .edit()
                .putString("jsonNierList", jsonString)
                .apply();

        Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteClick(int pos) {
        Intent intent = new Intent(this, OtherActivity.class);
        intent.putExtra("clicked_character", charArrayList.get(pos));
        startActivity(intent);
    }
}
