package com.dayquote.quotefortheday.models;

import io.realm.RealmObject;

public class QuoteDatabase extends RealmObject {

    private String quoteName;
    private String quoteAuthor;
    private String quoteWiki;

    public String getQuoteName() {
        return quoteName;
    }

    public void setQuoteName(String quoteName) {
        this.quoteName = quoteName;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteWiki() {
        return quoteWiki;
    }

    public void setQuoteWiki(String quoteWiki) {
        this.quoteWiki = quoteWiki;
    }
}
