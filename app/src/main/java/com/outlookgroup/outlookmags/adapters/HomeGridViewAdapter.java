package com.outlookgroup.outlookmags.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.Utils.Util;
import com.outlookgroup.outlookmags.modal.MagazineTypeVo;
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
            holder.relativeLytImg=(RelativeLayout) row.findViewById(R.id.relativeLytImg);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        CardView.LayoutParams lp = (CardView.LayoutParams) holder.image.getLayoutParams();
        if (Build.VERSION.SDK_INT<21){
            lp.width = ((width - Util.dipToPixels(context, 75)) / 2);
            lp.height = (int) (((width - Util.dipToPixels(context, 45)) / 2) * 1.4);
            Log.i("width",width+":"+Util.dipToPixels(context, 75)+":"+lp.width);
        }
        else {
            lp.width = ((width - Util.dipToPixels(context, 65)) / 2);
            lp.height = (int) (((width - Util.dipToPixels(context, 45)) / 2) * 1.4);
            Log.i("width",width+":"+Util.dipToPixels(context, 65)+":"+lp.width);
        }
        holder.image.setLayoutParams(lp);


        MagazineTypeVo magazine = data.get(position);
        holder.imageTitle.setText(magazine.getName().toUpperCase());
        if (!magazine.getCoverImage().isEmpty())
            Picasso.with(context).load(magazine.getCoverImage()).into(holder.image);
        Log.i("image",holder.image.getHeight()+":"+holder.image.getWidth());
        Log.i("lyt",holder.relativeLytImg.getHeight()+":"+holder.relativeLytImg.getWidth());
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        RelativeLayout relativeLytImg;
    }
}
