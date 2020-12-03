package com.dayquote.quotefortheday.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.jar.Attributes;

public class RealmFavoriteDeserialize {

    @SerializedName("quoteAuthorFav")
    @Expose
    private String quoteAuthorFav;
    @SerializedName("quoteNameFav")
    @Expose
    private String quoteNameFav;
    @SerializedName("quoteWikiFav")
    @Expose
    private String quoteWikiFav;

    public String getQuoteAuthorFav() {
        return quoteAuthorFav;
    }

    public void setQuoteAuthorFav(String quoteAuthorFav) {
        this.quoteAuthorFav = quoteAuthorFav;
    }

    public String getQuoteNameFav() {
        return quoteNameFav;
    }

    public void setQuoteNameFav(String quoteNameFav) {
        this.quoteNameFav = quoteNameFav;
    }

    public String getQuoteWikiFav() {
        return quoteWikiFav;
    }

    public void setQuoteWikiFav(String quoteWikiFav) {
        this.quoteWikiFav = quoteWikiFav;
    }

}