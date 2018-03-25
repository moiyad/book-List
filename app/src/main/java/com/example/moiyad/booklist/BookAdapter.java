package com.example.moiyad.booklist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Moiyad on 27/04/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    private Book[] books;
    public BookAdapter( Context context) {
        super(context, 0);
    }

    public void update(Book[] books){
        this.books = books;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return books==null?0:books.length;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String authors = null;
        Book current =books[position];
        if(current.getAuthors()!=null) {

            authors=current.getAuthors().toString();
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        TextView nameLayout =(TextView)view.findViewById(R.id.title);
        TextView viewLayout =(TextView)view.findViewById(R.id.author);

        nameLayout.setText(current.getTitle());
        viewLayout.setText(authors);
        return view;
    }
}
