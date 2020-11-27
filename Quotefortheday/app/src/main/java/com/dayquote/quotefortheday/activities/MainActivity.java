package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.models.QuoteDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //turn off night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Realm.init(this);
        //realm = Realm.getDefaultInstance();

        CreatePrePopulateDatabase();
        DatabaseLogic();

    }


    private void DatabaseLogic(){

        try {

            // wyświetlanie losowego cytatu
            RealmResults<QuoteDatabase> results = realm.where(QuoteDatabase.class).findAll();
            Random random = new Random();
            int randomQuote = random.nextInt(results.size());
            QuoteDatabase randomQuotePrinted = results.get(randomQuote);
            String quoteName = randomQuotePrinted.getQuoteName();
            String quoteAuthor = randomQuotePrinted.getQuoteAuthor();
            String quoteWiki = randomQuotePrinted.getQuoteWiki();

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

        RealmConfiguration config0 = new RealmConfiguration.Builder()
                .name("quotesDatabase.realm")
                .build();

        realm = Realm.getInstance(config0);

        //sprawdzenie czy istnieje baza oraz czy jest pusta- jeśli tak to kopiuje baze
        if(new File(config0.getPath()).exists()){
            Toast.makeText(this,"Database exist",Toast.LENGTH_LONG).show();
            RealmResults<QuoteDatabase> results = realm.where(QuoteDatabase.class).findAll();

            if(results.isEmpty()){
                copyBundledRealmFile(this.getResources().openRawResource(R.raw.quotes200), "quotesDatabase.realm");
                Toast.makeText(this,"Skopiowano baze, bo jest pusta",Toast.LENGTH_LONG).show();
            }

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
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about_app:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
     @Override
    protected void onDestroy () {
        realm.close();
        super.onDestroy();
    }
}