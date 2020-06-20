package com.example.projandroid3a;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.projandroid3a.data.NierAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.projandroid3a.data.Constants.BASE_URL;

public class Singletons {

    private static Gson gsonInstance;
    private static NierAPI nierAPIInstance;
    private static SharedPreferences sharedPreferencesInstance;

    public static Gson getGson(){
        if(gsonInstance == null){
            gsonInstance = new GsonBuilder()
                    .setLenient()
                    .create();
        }
        return gsonInstance;
    }

    public static NierAPI getNierAPI(){
        if(nierAPIInstance == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();

            nierAPIInstance = retrofit.create(NierAPI.class);
        }

        return nierAPIInstance;
    }

    public static SharedPreferences getSharedPreferences(Context context){
        if(sharedPreferencesInstance == null){
            sharedPreferencesInstance = context.getSharedPreferences("app_nier", Context.MODE_PRIVATE);
        }
        return sharedPreferencesInstance;
    }
}
