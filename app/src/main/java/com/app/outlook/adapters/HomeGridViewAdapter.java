package com.app.outlook.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.outlook.R;
import com.app.outlook.Utils.Util;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.MagazineTypeVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class HomeGridViewAdapter extends ArrayAdapter<MagazineTypeVo> {

    private final int layoutResourceId;
    private Context context;
    private ArrayList<MagazineTypeVo> data;
    private int width;

    public HomeGridViewAdapter(Context context, int layoutResourceId, ArrayList<MagazineTypeVo> data, int width) {
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
            holder.imageTitle = (TextView) row.findViewById(R.id.magazineName);
            holder.image = (ImageView) row.findViewById(R.id.magazineImg);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        CardView.LayoutParams lp = (CardView.LayoutParams) holder.image.getLayoutParams();
        lp.width = ((width - Util.dipToPixels(context, 30)) / 2);
        lp.height = (int) (((width - Util.dipToPixels(context, 30)) / 2) * 1.4);
        holder.image.setLayoutParams(lp);

        MagazineTypeVo magazine = data.get(position);
        holder.imageTitle.setText(magazine.getName());
        if(!magazine.getCoverImage().isEmpty())
            Picasso.with(context).load(magazine.getCoverImage()).fit().into(holder.image);

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
