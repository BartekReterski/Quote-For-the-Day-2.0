package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.adapters.FavoriteAdapter;
import com.dayquote.quotefortheday.models.FavoriteDatabase;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class FavoriteActivity extends AppCompatActivity {
    Realm realm;
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;

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

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        RealmResults<FavoriteDatabase> results = realm.where(FavoriteDatabase.class).findAll();
        for(FavoriteDatabase favoriteDatabase : results){

            favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,results);
            recyclerView.setAdapter(favoriteAdapter);
        }
        if(results.isEmpty()){

            Toast.makeText(FavoriteActivity.this,"Favorite Quotes is empty",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                    SortLogic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void SortLogic(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FavoriteActivity.this);
        alertDialog.setTitle("Sort");
        String[] items = {"Author(ascending)", "Author(descending)","Time added"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //sortowanie według ascending Autor
                        RealmResults<FavoriteDatabase> resultFavoritesAscending = realm.where(FavoriteDatabase.class).sort("quoteAuthorFav", Sort.ASCENDING).findAll();
                        for (FavoriteDatabase favoriteDatabase : resultFavoritesAscending) {

                            FavoriteAdapter favoriteAdapterAscending = new FavoriteAdapter(FavoriteActivity.this,resultFavoritesAscending);
                            recyclerView.setAdapter(favoriteAdapterAscending);
                        }

                        break;
                    case 1:
                        //sortowanie według descending Autor
                        RealmResults<FavoriteDatabase> resultFavoritesDscending = realm.where(FavoriteDatabase.class).sort("quoteAuthorFav", Sort.DESCENDING).findAll();
                        for (FavoriteDatabase favoriteDatabase : resultFavoritesDscending) {

                            FavoriteAdapter favoriteAdapterAscending = new FavoriteAdapter(FavoriteActivity.this,resultFavoritesDscending);
                            recyclerView.setAdapter(favoriteAdapterAscending);
                        }
                        break;
                    case 2:
                        //sortowanie według czasu dodania
                        RealmResults<FavoriteDatabase> results = realm.where(FavoriteDatabase.class).findAll();
                        for(FavoriteDatabase favoriteDatabase : results){

                            favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,results);
                            recyclerView.setAdapter(favoriteAdapter);
                        }

                        break;
                }
            }
        });
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = alertDialog.create();

        alert.show();
    }
    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}