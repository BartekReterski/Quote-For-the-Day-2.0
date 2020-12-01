package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.adapters.FavoriteAdapter;
import com.dayquote.quotefortheday.models.FavoriteDatabase;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FavoriteActivity extends AppCompatActivity {

    List<FavoriteDatabase> favoriteDatabaseList;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Favorite Quotes");
        Realm.init(this);
        RealmConfiguration favoriteConfig= new RealmConfiguration.Builder()
                .name("favoriteQuotes.realm")
                .build();

        realm= Realm.getInstance(favoriteConfig);
        LoadData();
    }

    private void LoadData(){

        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        RealmResults<FavoriteDatabase> results = realm.where(FavoriteDatabase.class).findAll();
        for(FavoriteDatabase favoriteDatabase : results){

            FavoriteAdapter favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,results);
            recyclerView.setAdapter(favoriteAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}