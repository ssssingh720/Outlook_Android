package com.app.outlook.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.modal.Magazine;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class OutlookGridViewAdapter extends ArrayAdapter<Magazine> {

    private final int layoutResourceId;
    private Context context;
    private ArrayList<Magazine> data;
    private int width;

    public OutlookGridViewAdapter(Context context, int layoutResourceId, ArrayList<Magazine> data,int width) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.width = width;
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
            holder.headerLyt = (LinearLayout)row.findViewById(R.id.headerLyt);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        CardView.LayoutParams lp = (CardView.LayoutParams) holder.image.getLayoutParams();
        lp.width = ((width- Util.dipToPixels(context, 40))/2);
        lp.height = (int) (((width- Util.dipToPixels(context, 40))/2)*1.4);
        holder.image.setLayoutParams(lp);

        Magazine magazine = data.get(position);
        holder.image.setImageResource(magazine.getImage());
        holder.dateTxt.setText(magazine.getIssueDate());
        if(position > 0 && data.get(position-1).getIssueDate().equals(magazine.getIssueDate())) {
            holder.dateTxt.setText("");
        }
        if(position > 1 && data.get(position-1).getIssueDate().equals(magazine.getIssueDate())
                && data.get(position-2).getIssueDate().equals(magazine.getIssueDate())) {
            holder.headerLyt.setVisibility(View.GONE);
        }else{
            holder.headerLyt.setVisibility(View.VISIBLE);
        }

        return row;
    }

    static class ViewHolder {
        TextView dateTxt;
        ImageView image;
        LinearLayout headerLyt;
    }
}
