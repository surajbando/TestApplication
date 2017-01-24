package com.testapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Bando on 25-01-2017.
 */

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
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView rowTitle = (TextView) rowView.findViewById(R.id.row_title);
        TextView rowDesc = (TextView) rowView.findViewById(R.id.row_desc);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.row_img);

        rowTitle.setText(rowEntries[position].getTitle());
        rowDesc.setText(rowEntries[position].getDescription());
        Picasso.with(context)
                .load(rowEntries[position].getImageHref())
                .into(rowImage);

        return rowView;
    }

}
