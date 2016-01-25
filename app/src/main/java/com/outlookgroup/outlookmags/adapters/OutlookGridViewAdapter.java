package com.outlookgroup.outlookmags.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outlookgroup.outlookmags.R;
import com.outlookgroup.outlookmags.Utils.Util;
import com.outlookgroup.outlookmags.listener.OnIssueItemsClickListener;
import com.outlookgroup.outlookmags.manager.SharedPrefManager;
import com.outlookgroup.outlookmags.modal.Magazine;
import com.outlookgroup.outlookmags.modal.OutlookConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by srajendrakumar on 09/09/15.
 */
public class OutlookGridViewAdapter extends ArrayAdapter<Magazine> {

    private final int layoutResourceId;
    private Context context;
    private ArrayList<Magazine> data;
    private int width;
    private String magazineID;
    private OnIssueItemsClickListener onIssueItemsClickListener;

    public OutlookGridViewAdapter(Context context, int layoutResourceId, ArrayList<Magazine> data, int width,
                                  String magazineID,OnIssueItemsClickListener onIssueItemsClickListener) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.width = width;
        this.magazineID = magazineID;
        this.onIssueItemsClickListener = onIssueItemsClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.magazineImg);
            holder.dateTxt = (TextView) row.findViewById(R.id.dateTxt);
            holder.headerLyt = (LinearLayout) row.findViewById(R.id.headerLyt);
            holder.mainLyt = (LinearLayout) row.findViewById(R.id.mainLyt);
            holder.lytMagazine=(RelativeLayout)row.findViewById(R.id.LytMagazine);
            holder.buyBtn = (Button) row.findViewById(R.id.buyBtn);
            holder.draftText=(TextView) row.findViewById(R.id.draftText);
            if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
                holder.buyBtn.setVisibility(View.GONE);
                holder.draftText.setVisibility(View.VISIBLE);
            }
            if (magazineID.equals("5")){
                holder.buyBtn.setVisibility(View.GONE);
            }
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) holder.lytMagazine.getLayoutParams();
        lp.width = ((width - Util.dipToPixels(context, 65)) / 2);
        lp.height = (int) (((width - Util.dipToPixels(context, 45)) / 2) * 1.4);
        holder.lytMagazine.setLayoutParams(lp);

        Magazine magazine = data.get(position);
        if (magazine.getPostId() == null) {
            holder.mainLyt.setVisibility(View.GONE);
            return row;
        } else {
            holder.mainLyt.setVisibility(View.VISIBLE);
        }
        if (magazine.getImage()!=null && !magazine.getImage().isEmpty()) {
            Picasso.with(context).load(magazine.getImage())
                    .placeholder(R.drawable.outlook).error(R.drawable.outlook).into(holder.image);

               /* if(holder.image.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.outlook).getConstantState()){
                    Log.i("getDrawable",magazine.getImage());
                    //do work here
                    try {
                        URL url = new URL(magazine.getImage());
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        holder.image.setImageBitmap(image);
                    }
                    catch (MalformedURLException e){

                    }
                    catch (IOException e){

                    }
                }*/
        } else {
            holder.image.setImageResource(R.drawable.outlook);
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIssueItemsClickListener.onCoverImageClicked(position);
            }
        });

        holder.dateTxt.setText(magazine.getIssueDate());
        if (position > 0 && data.get(position - 1).getIssueDate() != null
                && data.get(position - 1).getIssueDate().equals(magazine.getIssueDate())) {
            holder.dateTxt.setText("");
        }
        if (position > 1 && data.get(position - 1).getIssueDate() != null
                && data.get(position - 2).getIssueDate() != null
                && data.get(position - 1).getIssueDate().equals(magazine.getIssueDate())
                && data.get(position - 2).getIssueDate().equals(magazine.getIssueDate())) {
            holder.headerLyt.setVisibility(View.GONE);
        } else {
            holder.headerLyt.setVisibility(View.VISIBLE);
        }


        if (magazine.isPurchased()) {
            if (Util.checkFiledownLoaded(context.getCacheDir().getAbsolutePath(), magazineID, magazine.getPostId())) {
                holder.buyBtn.setVisibility(View.GONE);
            } else {
                holder.buyBtn.setText(context.getResources().getString(R.string.download));
                holder.buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIssueItemsClickListener.onDownloadClicked(position);
                    }
                });
            }
        } else {
            holder.buyBtn.setText(context.getResources().getString(R.string.buy));
            // Hiding Buy button : make visible for payment
            holder.buyBtn.setVisibility(View.GONE);
            holder.buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIssueItemsClickListener.onBuyClicked(position);
                }
            });
        }
        if (magazineID.equals("5")){
            holder.buyBtn.setVisibility(View.GONE);
        }
        if (SharedPrefManager.getInstance().getSharedDataBoolean(OutlookConstants.IS_ADMIN)){
            holder.buyBtn.setVisibility(View.GONE);
            holder.draftText.setVisibility(View.VISIBLE);
        }
        return row;
    }

    @Override
    public Magazine getItem(int position) {
        return data.get(position);
    }

    static class ViewHolder {
        TextView dateTxt,draftText;
        ImageView image;
        LinearLayout headerLyt, mainLyt;
        RelativeLayout lytMagazine;
        Button buyBtn;
    }
}
