package com.app.outlook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.modal.Magazine;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class CategoryGridViewAdapter extends ArrayAdapter<Magazine> {

    private final int layoutResourceId;
    private Context context;
    private ArrayList<Magazine> data;

    public CategoryGridViewAdapter(Context context, int layoutResourceId, ArrayList<Magazine> data) {
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
            holder.image = (ImageView) row.findViewById(R.id.magazineImg);
            holder.dateTxt = (TextView) row.findViewById(R.id.dateTxt);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Magazine magazine = data.get(position);
        holder.image.setImageResource(magazine.getImage());
        holder.dateTxt.setText(magazine.getIssueDate());

        return row;
    }

    static class ViewHolder {
        TextView dateTxt;
        ImageView image;
    }
}
