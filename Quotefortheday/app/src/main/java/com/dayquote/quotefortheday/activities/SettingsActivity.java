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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.adapters.SettingsAdapter;
import com.dayquote.quotefortheday.models.SettingsModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class SettingsActivity extends AppCompatActivity {

    SettingsModel settingsModel;
    private static final int PICK_IMAGE = 100;
    SharedPreferences sharedPreferencesSetFromDisk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Themes");

        RecyclerView recyclerView= findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(SettingsActivity.this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        List<SettingsModel> settingsModelList = new ArrayList<>();

        settingsModel = new SettingsModel("Wall",R.drawable.image_a);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_b);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Laka",R.drawable.image_c);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Las",R.drawable.image_d);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Wall",R.drawable.image_e);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_f);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Wall",R.drawable.image_g);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_h);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_i);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_j);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_k);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_l);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_m);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_n);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_o);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_p);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_s);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_t);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_w);
        settingsModelList.add(settingsModel);
        settingsModel = new SettingsModel("Choinka",R.drawable.image_y);
        settingsModelList.add(settingsModel);


        SettingsAdapter settingsAdapter = new SettingsAdapter(SettingsActivity.this,settingsModelList);
        recyclerView.setAdapter(settingsAdapter);

        Button buttonGetImage =findViewById(R.id.button_get_image);
        buttonGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri = data.getData();

            //usuniecie sharedpreferences z drawable slot
            SharedPreferences sharedPreferencesRemove=getSharedPreferences("PREFS_BACK",MODE_PRIVATE);
            sharedPreferencesRemove.edit().remove("background").apply();

            //wysłanie danych tymczasowych shared preferences
            sharedPreferencesSetFromDisk=getSharedPreferences("PREFS_BACK_DISK",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferencesSetFromDisk.edit();
            editor.putString("backgroundFromDisk", String.valueOf(imageUri));
            editor.apply();

            data= new Intent(SettingsActivity.this,MainActivity.class);
            data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(data);

            Toast.makeText(SettingsActivity.this,"Theme changed",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        //odebranie danych na temat ustawienia checkboxa
        sharedPreferencesSetFromDisk=getSharedPreferences("PREFS_BACK_DISK",MODE_PRIVATE);
        boolean check_box_value=sharedPreferencesSetFromDisk.getBoolean("check_black_font",false);
        menu.findItem(R.id.action_black_font).setChecked(check_box_value);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_color_toolbar:

                return true;
            case R.id.action_black_font:
                sharedPreferencesSetFromDisk=getSharedPreferences("PREFS_BACK_DISK",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferencesSetFromDisk.edit();
                if(item.isChecked()){
                    item.setChecked(false);
                    editor.putBoolean("check_black_font",false);
                    editor.apply();
                    Intent intent= new Intent(SettingsActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{

                    item.setChecked(true);
                    editor.putBoolean("check_black_font",true);
                    editor.apply();
                    Intent intent= new Intent(SettingsActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}