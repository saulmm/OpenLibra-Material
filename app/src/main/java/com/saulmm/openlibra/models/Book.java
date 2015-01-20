package com.saulmm.openlibra.models;

import java.io.Serializable;

public class Book implements Serializable {

    private String title;
    private String cover;
    private String author;
    private String content;
    private String publisher;
    private String publisher_date;
    private String pages;
    private String rating;

    public String getContent() {

        if (content != null) {

            content = replaceHtmlEntities(content);
        }

        return content;
    }

    public String replaceHtmlEntities (String content) {

        return content.replace("&Atilde&iexcl","á")
            .replace("&Atilde&copy","e")
            .replace("copy;","e")
            .replace("&Atilde&shy","i")
            .replace(";shy","i")
            .replace("&Atilde&sup3","ó")
            .replace("sup3;","ó")
            .replace("sup3;","ó")
            .replace("&Atilde;&plusmn","ñ")
            .replace("&Atilde;&","")
            .replace("&lt;ul&gt;", "")
            .replace("&lt;/li&gt;", "")
            .replace("&lt;/ul&gt;", "")
            .replace("&lt;li&gt;", "\n\t\t");

    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublisher_date() {
        return publisher_date;
    }

    public String getPages() {
        return pages;
    }

    public String getRating() {
        return rating;
    }

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
