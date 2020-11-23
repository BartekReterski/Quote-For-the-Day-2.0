package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dayquote.quotefortheday.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();



    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }


    private void CreatePrePopulateDatabase(){

        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default0), "default0.realm");

        RealmConfiguration config0 = new RealmConfiguration.Builder()
                .name("default0.realm")
                .build();

        realm = Realm.getInstance(config0);


        }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
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
}