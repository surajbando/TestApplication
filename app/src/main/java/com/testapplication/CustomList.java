package com.testapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/*
This class is used to dynamically load the listView
* */

public class CustomList extends ArrayAdapter<RowEntry> {
    private final Activity context;
    private final RowEntry[] rowEntries;

    public CustomList(Activity context,
                      RowEntry[] rowEntries) {
        super(context, R.layout.list_single, rowEntries);
        this.context = context;
        this.rowEntries = rowEntries;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_single, null, true);
        TextView rowTitleView = (TextView) rowView.findViewById(R.id.row_title);
        TextView rowDescView = (TextView) rowView.findViewById(R.id.row_desc);
        ImageView rowImageView = (ImageView) rowView.findViewById(R.id.row_img);

        rowTitleView.setText(rowEntries[position].getTitle());
        rowDescView.setText(rowEntries[position].getDescription());

        Picasso.with(context)
                .load(rowEntries[position].getImageHref())
                .resize(300,200)
                .centerCrop()
                .placeholder(R.drawable.no_thumb)
                .error(R.drawable.error_thumb)
                .into(rowImageView);

        return rowView;
    }

}
