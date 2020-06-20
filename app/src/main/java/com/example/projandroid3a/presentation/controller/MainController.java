package com.example.projandroid3a.presentation.controller;

import android.content.Intent;
import android.content.SharedPreferences;

import com.example.projandroid3a.Singletons;
import com.example.projandroid3a.presentation.model.NierCharacter;
import com.example.projandroid3a.presentation.model.RestNierAPIResponse;
import com.example.projandroid3a.presentation.view.MainActivity;
import com.example.projandroid3a.presentation.view.OtherActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainController {

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private MainActivity view;
    private ArrayList<NierCharacter> charArrayList;
    public MainController(MainActivity main, Gson gson, SharedPreferences sharedPreferences){
        this.view = main;
        this.gson = gson;
        this.sharedPreferences = sharedPreferences;
    }

    public void onStart(){
        List<NierCharacter> charaList = getDataFromCache();
        if(charaList != null){
            view.showList(charaList);
        }else {
            makeApiCall();
        }
        charArrayList = getArrayDataFromCache();
        if(charArrayList == null){
            makeApiCall();
        }
    }

    private void makeApiCall(){
        Call<RestNierAPIResponse> call = Singletons.getNierAPI().getNierResponse();
        call.enqueue(new Callback<RestNierAPIResponse>() {
            @Override
            public void onResponse(Call<RestNierAPIResponse> call, Response<RestNierAPIResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<NierCharacter> charaList = response.body().getResults();
                    saveList(charaList);
                    view.showList(charaList);
                }
            }

            @Override
            public void onFailure(Call<RestNierAPIResponse> call, Throwable t) {
                view.showError();
            }
        });
    }

    private void saveList(List<NierCharacter> charaList) {
        String jsonString = gson.toJson(charaList);
        sharedPreferences
                .edit()
                .putString("jsonNierList", jsonString)
                .apply();
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

    public void startOtherActivity(int pos){
        Intent intent = new Intent(view, OtherActivity.class);
        intent.putExtra("clicked_character", charArrayList.get(pos));
        view.startActivity(intent);
    }

    public ArrayList<NierCharacter> getCharArrayList(){
        return charArrayList;
    }
}
