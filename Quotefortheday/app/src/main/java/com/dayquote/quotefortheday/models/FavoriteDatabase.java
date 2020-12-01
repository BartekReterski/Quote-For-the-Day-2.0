package com.dayquote.quotefortheday.models;

import io.realm.RealmObject;

public class FavoriteDatabase extends RealmObject {

    private String quoteNameFav;
    private String quoteAuthorFav;
    private String quoteWikiFav;


    public String getQuoteNameFav() {
        return quoteNameFav;
    }

    public void setQuoteNameFav(String quoteNameFav) {
        this.quoteNameFav = quoteNameFav;
    }

    public String getQuoteAuthorFav() {
        return quoteAuthorFav;
    }

    public void setQuoteAuthorFav(String quoteAuthorFav) {
        this.quoteAuthorFav = quoteAuthorFav;
    }

    public String getQuoteWikiFav() {
        return quoteWikiFav;
    }

    public void setQuoteWikiFav(String quoteWikiFav) {
        this.quoteWikiFav = quoteWikiFav;
    }
}
