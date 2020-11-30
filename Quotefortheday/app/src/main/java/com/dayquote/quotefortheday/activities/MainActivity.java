package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.models.QuoteDatabase;
import com.dayquote.quotefortheday.services.AlarmReceiver;
import com.eggheadgames.realmassethelper.IRealmAssetHelperStorageListener;
import com.eggheadgames.realmassethelper.RealmAssetHelper;
import com.eggheadgames.realmassethelper.RealmAssetHelperStatus;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    int hourShared;
    int minuteShared;
    private int notificationId = 1;
    Realm realm;
    RealmConfiguration realmConfig;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesCalendar;
    String quoteName;
    String quoteWiki;
    String quoteAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //turn off night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Realm.init(this);
         CreatePrePopulateDatabase();
         FloatedButtonsLogic();
         DatabaseLogic();

        //odebranie danych z shared preferences
        SharedPreferences sharedPreferences1=getSharedPreferences("PREFS",MODE_PRIVATE);
        quoteName= sharedPreferences1.getString("quoteName","null2");
        quoteAuthor=sharedPreferences1.getString("quoteAuthor","null2");
        quoteWiki=sharedPreferences1.getString("quoteWiki","null");
        TextView quoteText= findViewById(R.id.quoteText);
        quoteText.setText(quoteName+" \n\n"+" - "+quoteAuthor);
       // Action24h();


    }

    private void Action24h(){
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        sharedPreferencesCalendar = getSharedPreferences("PREFS", 0);
        int lastDay = sharedPreferencesCalendar.getInt("day", 0);

        if (lastDay != currentDay) {
            SharedPreferences.Editor editor = sharedPreferencesCalendar.edit();
            editor.putInt("day", currentDay);
            editor.apply();


        }
    }

    //logika bazy danych wraz z usuwaniem cytatu codziennym
    private void DatabaseLogic(){

        try {
            // wyświetlanie losowego cytatu
            RealmResults<QuoteDatabase> results = realm.where(QuoteDatabase.class).findAll();
            Random random = new Random();
            int randomQuote = random.nextInt(results.size());
            QuoteDatabase randomQuotePrinted = results.get(randomQuote);
            assert randomQuotePrinted != null;
            String quoteName = randomQuotePrinted.getQuoteName();
            String quoteAuthor = randomQuotePrinted.getQuoteAuthor();
            String quoteWiki = randomQuotePrinted.getQuoteWiki();

            //wysłanie danych tymczasowych shared preferences
            sharedPreferences=getSharedPreferences("PREFS",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("quoteName",quoteName);
            editor.putString("quoteAuthor",quoteAuthor);
            editor.putString("quoteWiki",quoteWiki);
            editor.apply();

            //usuwanie wybranego cytatu
            RealmResults<QuoteDatabase> rows = realm.where(QuoteDatabase.class).equalTo("quoteName", quoteName).findAll();
            realm.beginTransaction();
            rows.deleteAllFromRealm();
            realm.commitTransaction();

        }catch (Exception ex){

            System.out.println(ex.getMessage());
        }

    }

    //stworzenie załadowanej bazy
    private void CreatePrePopulateDatabase () {
        try {
            //wczytanie bazy danych domyślnej
            RealmAssetHelper.getInstance(this).loadDatabaseToStorage("database", "quotes200", new IRealmAssetHelperStorageListener() {
                @Override
                public void onLoadedToStorage(String realmDbName, RealmAssetHelperStatus status) {
                   realmConfig = new RealmConfiguration.Builder()
                            .name(realmDbName)
                            .build();
                    realm = Realm.getInstance(realmConfig);

                }
        });
            //jeśli baza jest pusta wczyanie raz jeszcze innej
            RealmResults<QuoteDatabase> results = realm.where(QuoteDatabase.class).findAll();
            if(results.size()==0){
                Toast.makeText(MainActivity.this,"jest pusta",Toast.LENGTH_SHORT).show();
                realmConfig = new RealmConfiguration.Builder()
                        .name("quote200.realm")
                        .build();
                realm = Realm.getInstance(realmConfig);
                copyBundledRealmFile(this.getResources().openRawResource(R.raw.quotes200),"quotes200.realm");
            }

        }catch (Exception ex){

            System.out.println(ex.getMessage());
        }
    }

    //pobranie bazy danych z plikun.realm
    private String copyBundledRealmFile (InputStream inputStream, String outFileName){
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:

                Notifications();
                return true;
            case R.id.action_about_app:
               // Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void FloatedButtonsLogic(){
        FloatingActionButton mShare= findViewById(R.id.material_design_floating_action_menu_item_share);
        FloatingActionButton mFavorite=findViewById(R.id.material_design_floating_action_menu_item_favorite);
        FloatingActionButton mWiki= findViewById(R.id.material_design_floating_action_menu_item_wiki);
        FloatingActionMenu mMenu= findViewById(R.id.material_design_android_floating_action_menu);

        mWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,WikiActivity.class);
                intent.putExtra("quoteWiki",quoteWiki);
                intent.putExtra("quoteAuthor",quoteAuthor);
                startActivity(intent);
            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.close(true);
                Toast.makeText(MainActivity.this,"Taking screenshot, Please wait",Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveBitmap();

                    }
                }, 1000);
            }

        });

    }

//zapisanie screenshot w postaci bitmapy
    public void saveBitmap() {
        RuntimePermissions();
        Bitmap bitmap= getBitmapFromView();
        try {

            File defaultFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dailyQuotes");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = quoteName+".jpg";

            File file = new File(defaultFile,filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile,filename);
            }

            Uri imageUri = FileProvider.getUriForFile(
                    MainActivity.this,
                    "com.example.homefolder.example.provider", //(use your app signature + ".provider" )
                    file);

            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent sendScreenShot= new Intent(Intent.ACTION_SEND);
            sendScreenShot.setType("image/*");
            sendScreenShot.putExtra(Intent.EXTRA_STREAM,imageUri);
            startActivity(Intent.createChooser(sendScreenShot,"Share via:"));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private Bitmap getBitmapFromView() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
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
                Toast.makeText(MainActivity.this, "Permissions denied", Toast.LENGTH_LONG).show();
            }
        };


        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onDestroy () {
        realm.close();
        super.onDestroy();
    }

    private void Notifications(){

        //odebranie danych tymczasowych na temat godziny
        SharedPreferences sharedPreferences1 = getSharedPreferences("PREFS", MODE_PRIVATE);
        hourShared = sharedPreferences1.getInt("hour", 0);
        minuteShared = sharedPreferences1.getInt("minute", 0);

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.notification_alert_dialog, viewGroup, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialogNotification = builder.create();
        Objects.requireNonNull(alertDialogNotification.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogNotification.show();

        TextView notificationText = alertDialogNotification.findViewById(R.id.timeNotification);
        ImageView deleteNotification= alertDialogNotification.findViewById(R.id.deleteNotification);

        //ustawienie widocznosci jesli zawartosc shared prefernces nie jest pusta
        if(hourShared==0 && minuteShared==0){


            notificationText.setText("notification time");
            deleteNotification.setVisibility(View.GONE);
        }else{


            notificationText.setText(String.format("Notification time is " + "%02d:%02d", hourShared, minuteShared));
            deleteNotification.setVisibility(View.VISIBLE);

        }

        Button chooseTime = alertDialogNotification.findViewById(R.id.setNotificationButton);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Intent
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                intent.putExtra("notificationId", notificationId);

                // PendingIntent
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                );

                // AlarmManager
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //wyslanie danych tymczasowych na temat wybranej godziny
                                sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("hour", hourOfDay);
                                editor.putInt("minute", minute);
                                editor.apply();


                                //odebranie danych tymczasowych na temat godziny
                                SharedPreferences sharedPreferences1 = getSharedPreferences("PREFS", MODE_PRIVATE);
                                hourShared = sharedPreferences1.getInt("hour", 0);
                                minuteShared = sharedPreferences1.getInt("minute", 0);


                                notificationText.setText(String.format("Notification time is " + "%02d:%02d", hourShared, minuteShared));

                                // ustawienie czasu alarmu
                                Calendar startTime = Calendar.getInstance();
                                startTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startTime.set(Calendar.MINUTE, minute);
                                startTime.set(Calendar.SECOND, 0);
                                long alarmStartTime = startTime.getTimeInMillis();

                                // ustawienie alarmu
                                Objects.requireNonNull(alarmManager).set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
                                alertDialogNotification.dismiss();
                                Toast.makeText(getApplicationContext(),String.format("Notification time is " + "%02d:%02d", hourShared, minuteShared),Toast.LENGTH_LONG).show();

                            }
                        }, mHour, mMinute, true);

                timePickerDialog.show();

            }
        });


        deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent
                Intent intent2 = new Intent(MainActivity.this, AlarmReceiver.class);
                intent2.putExtra("notificationId", notificationId);

                // PendingIntent
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                        MainActivity.this, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT
                );

                // AlarmManager
                AlarmManager alarmManagerCancel = (AlarmManager)getSystemService(ALARM_SERVICE);

                Objects.requireNonNull(alarmManagerCancel).cancel(pendingIntent2);


                //anulowanie  danych tymczasowych na temat wybranej godziny
                sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("hour");
                editor.remove("minute");
                editor.apply();

                alertDialogNotification.dismiss();
                Toast.makeText(getApplicationContext(),"Canceled notification", Toast.LENGTH_SHORT).show();
            }
        });



    }

}