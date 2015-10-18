package com.app.outlook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 15/10/15.
 */
public class GridListAdapter extends ArrayAdapter<String> {

    private final int layoutResourceId;
    private Context context;
    private ArrayList<String> data;
    private int width;

    public GridListAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
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
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        return row;
    }

    static class ViewHolder {
        TextView dateTxt;
        ImageView image;
        LinearLayout headerLyt;
    }
}
