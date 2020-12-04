package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.adapters.FavoriteAdapter;
import com.dayquote.quotefortheday.models.FavoriteDatabase;
import com.dayquote.quotefortheday.models.RealmFavoriteDeserialize;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
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

            case R.id.action_save_to_file:
                SavetoFile();
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

    //eksport ulubionych cytatów do pliku tekstowego
    private void SavetoFile(){
        
        RuntimePermissions();
        try {
            RealmResults<FavoriteDatabase> realmObj = realm.where(FavoriteDatabase.class).findAll();
            String allResult=new Gson().toJson(realm.copyFromRealm(realmObj));

            File defaultFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/dailyQuotes");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "favoriteQuotes.txt";

            File file = new File(defaultFile, filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile, filename);
            }
            FileOutputStream output = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter= new OutputStreamWriter(output);
            Gson gson= new Gson();
            Type type = new TypeToken<List<RealmFavoriteDeserialize>>(){}.getType();
            List<RealmFavoriteDeserialize> contactList = gson.fromJson(allResult, type);
            for (RealmFavoriteDeserialize contact : contactList){
                Log.i("Contact Details", contact.getQuoteNameFav() + "-" + contact.getQuoteAuthorFav() + "-" + contact.getQuoteWikiFav());
                outputStreamWriter.write(" \n\n"+contact.getQuoteNameFav()+" \n\n"+" - "+contact.getQuoteAuthorFav());

            }
            Toast.makeText(FavoriteActivity.this,"File was saved in"+defaultFile,Toast.LENGTH_LONG).show();
            outputStreamWriter.close();


        }catch (Exception exception){

            System.out.println(exception.getMessage());
        }

    }

    //zadeklarowanie uprawnień
    private void RuntimePermissions() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //    Toast.makeText(MainActivity.this, "Permissions granted", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FavoriteActivity.this, "Permissions denied", Toast.LENGTH_LONG).show();
            }
        };


        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}