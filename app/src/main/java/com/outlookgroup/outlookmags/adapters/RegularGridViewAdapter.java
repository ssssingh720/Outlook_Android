package com.outlookgroup.outlookmags.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.modal.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class RegularGridViewAdapter extends ArrayAdapter<Category> {

    private final int layoutResourceId;
    private Context context;
    private ArrayList<Category> data;

    public RegularGridViewAdapter(Context context, int layoutResourceId, ArrayList<Category> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.categoryImg);
            holder.nameTxt = (TextView) row.findViewById(R.id.categoryName);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Category category = data.get(position);
        if(category.getCategoryIcon()!= null && !category.getCategoryIcon().isEmpty())
            Picasso.with(context).load(category.getCategoryIcon()).fit().into(holder.image);

        if(category.getCategoryName() != null && !category.getCategoryName().isEmpty())
            holder.nameTxt.setText(category.getCategoryName());

        return row;
    }

    static class ViewHolder {
        TextView nameTxt;
        ImageView image;
    }
}
