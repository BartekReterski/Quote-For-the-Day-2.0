package com.dayquote.quotefortheday.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dayquote.quotefortheday.R;
import com.dayquote.quotefortheday.models.FavoriteDatabase;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

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
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        FavoriteDatabase favorite = favoriteDatabaseList.get(position);

        String favoriteQuoteAuthor=favorite.getQuoteAuthorFav();
        String favoriteQuoteWiki=favorite.getQuoteWikiFav();

        holder.favoriteQuoteName.setText(favorite.getQuoteNameFav()+" \n\n"+" - "+favoriteQuoteAuthor);
        holder.favoriteMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}

