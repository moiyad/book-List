package com.example.moiyad.booklist;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Moiyad on 27/04/17.
 */

public class Book {
    private String title;
    private JSONArray authors;

    public Book(){

    }

    public Book(String title,JSONArray authors){
        this.title = title;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONArray getAuthors() {
        return authors;
    }

    public void setAuthors(JSONArray authors) {
        this.authors = authors;
    }

    public void display(){
        Log.d("book_title",title);
        if (authors != null) {
            for (int i = 0; i < authors.length(); i++) {
                try {
                    Log.d("book_authors", authors.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
