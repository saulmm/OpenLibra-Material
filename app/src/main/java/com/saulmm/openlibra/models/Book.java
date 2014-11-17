package com.saulmm.openlibra.models;

import android.support.v7.graphics.Palette;

import java.io.Serializable;

public class Book implements Serializable {

    private String title;
    private String cover;
    private String author;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
