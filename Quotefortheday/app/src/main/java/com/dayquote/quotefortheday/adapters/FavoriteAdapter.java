package com.dayquote.quotefortheday.adapters;

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

import androidx.recyclerview.widget.RecyclerView;

import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.activities.MainActivity;
import com.dayquote.quotefortheday.activities.WikiActivity;
import com.dayquote.quotefortheday.models.FavoriteDatabase;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    Realm realm;
    private Context mCtx;
    private List<FavoriteDatabase> favoriteDatabaseList;

    public FavoriteAdapter(Context mCtx, List<FavoriteDatabase> favoriteDatabaseList) {
        this.mCtx = mCtx;
        this.favoriteDatabaseList = favoriteDatabaseList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.favorite_items, null);

        //pobranie konfiguracji instancji realm favorite
        Realm.init(mCtx);
        RealmConfiguration favoriteConfig= new RealmConfiguration.Builder()
                .name("favoriteQuotes.realm")
                .build();

        realm= Realm.getInstance(favoriteConfig);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        FavoriteDatabase favorite = favoriteDatabaseList.get(position);

        String favoriteQuoteAuthor=favorite.getQuoteAuthorFav();
        String favoriteQuoteWiki=favorite.getQuoteWikiFav();
        String deleteQuoteName=favorite.getQuoteNameFav();

        holder.favoriteQuoteName.setText(favorite.getQuoteNameFav()+" \n\n"+" - "+favoriteQuoteAuthor);
        holder.favoriteMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mCtx,holder.favoriteMoreOption);
                popup.inflate(R.menu.context_menu_favorite_adapter);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.shareItem:

                                Intent txtShare = new Intent(android.content.Intent.ACTION_SEND);
                                txtShare .setType("text/plain");
                                txtShare .putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                                txtShare .putExtra(android.content.Intent.EXTRA_TEXT,  deleteQuoteName+" \n\n"+" - "+favoriteQuoteAuthor);
                                mCtx.startActivity(Intent.createChooser(txtShare ,"Share via:"));
                                break;

                            case R.id.wikiItem:

                                Intent intentWiki = new Intent(mCtx, WikiActivity.class);
                                intentWiki.putExtra("quoteWiki",favoriteQuoteWiki);
                                intentWiki.putExtra("quoteAuthor",favoriteQuoteAuthor);
                                mCtx.startActivity(intentWiki);
                                break;
                            case R.id.deleteItem:

                                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                        mCtx);
                                alertDialog2.setTitle("Confirm Delete");
                                        alertDialog2.setMessage("Are you sure you want to delete this quote: "+holder.favoriteQuoteName.getText().toString());
                                alertDialog2.setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                try {
                                                    RealmResults<FavoriteDatabase> results = realm.where(FavoriteDatabase.class).equalTo("quoteNameFav", deleteQuoteName).findAll();

                                                    realm.beginTransaction();
                                                    results.deleteAllFromRealm();
                                                    realm.commitTransaction();
                                                    notifyDataSetChanged();
                                                    Toast.makeText(mCtx,"Deleted",Toast.LENGTH_LONG).show();
                                                }catch (Exception ex){

                                                    System.out.println(ex.getMessage());
                                                }


                                            }
                                        });
                                alertDialog2.setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog
                                                dialog.cancel();
                                            }
                                        });

                                alertDialog2.show();

                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return favoriteDatabaseList.size();
    }


    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteQuoteName;
        ImageView favoriteMoreOption;

        public FavoriteViewHolder(View itemView) {
            super(itemView);

            favoriteQuoteName=itemView.findViewById(R.id.quoteTextFavorite);
            favoriteMoreOption = itemView.findViewById(R.id.more_option);
        }
    }
    public void onDestroy() {
        realm.close();

    }
}

