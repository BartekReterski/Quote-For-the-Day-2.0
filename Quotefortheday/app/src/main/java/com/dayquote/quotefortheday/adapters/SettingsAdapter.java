package com.dayquote.quotefortheday.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.activities.MainActivity;
import com.dayquote.quotefortheday.models.SettingsModel;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    private Context mCtx;
    private List<SettingsModel> settingsModelsList;

    public SettingsAdapter(Context mCtx, List<SettingsModel> settingsModelsList) {
        this.mCtx = mCtx;
        this.settingsModelsList = settingsModelsList;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.settings_items, null);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        SettingsModel settingsModel = settingsModelsList.get(position);

       //holder.imageBackground.setImageResource(settingsModel.getImage());
        Glide.with(mCtx).load(settingsModel.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageBackground);
       holder.imageDescription.setText(settingsModel.getImageDescription());
       int uriPath=settingsModel.getImage();


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //wysłanie danych tymczasowych shared preferences
               SharedPreferences sharedPreferences=mCtx.getSharedPreferences("PREFS_BACK",MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedPreferences.edit();
               editor.putString("background", String.valueOf(uriPath));
               editor.apply();
               Intent intent = new Intent(mCtx,MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
               mCtx.startActivity(intent);
               Toast.makeText(mCtx,"Theme changed",Toast.LENGTH_LONG).show();

           }
       });
    }


    @Override
    public int getItemCount() {
        return settingsModelsList.size();
    }


    class SettingsViewHolder extends RecyclerView.ViewHolder {

        TextView imageDescription;
        ImageView imageBackground;


        public SettingsViewHolder(View itemView) {
            super(itemView);

           imageDescription= itemView.findViewById(R.id.background_Image_Description);
           imageBackground=itemView.findViewById(R.id.background_Image);


        }
    }

}

