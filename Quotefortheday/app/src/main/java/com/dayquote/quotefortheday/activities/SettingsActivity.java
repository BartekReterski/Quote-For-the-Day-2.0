package com.dayquote.quotefortheday.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.adapters.SettingsAdapter;
import com.dayquote.quotefortheday.models.SettingsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class SettingsActivity extends AppCompatActivity {


    SettingsModel settingsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(SettingsActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<SettingsModel> settingsModelList = new ArrayList<>();

        settingsModel = new SettingsModel("Wall",R.drawable.ic_launcher_background);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.ic_launcher_background);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Wall",R.drawable.wall);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.okk);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Wall",R.drawable.wall);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.okk);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Wall",R.drawable.wall);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.okk);
        settingsModelList.add(settingsModel);

        SettingsAdapter settingsAdapter = new SettingsAdapter(SettingsActivity.this,settingsModelList);
        recyclerView.setAdapter(settingsAdapter);


    }
}