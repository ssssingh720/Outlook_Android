package com.app.outlook.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.adapters.OutlookGridViewAdapter;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.modal.Acf;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.IssuesVo;
import com.app.outlook.modal.Magazine;
import com.app.outlook.modal.WeeklyIssueVo;
import com.app.outlook.views.MonthYearPicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 10/09/15.
 */
public class CategoryListingActivity extends AppBaseActivity {

    private static final String TAG = "CategoryListingActivity";
    private OutlookGridViewAdapter adapter;
    @Bind(R.id.gridView)
    GridView gridView;
    private int magazineType;
    private LoadToast loadToast;
    private String issueYear = "2015";
    private String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_listing);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        root = Environment.getExternalStorageDirectory().getAbsoluteFile().toString();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        loadToast = new LoadToast(this);
        loadToast.setText("Downloading...");
        int height = size.y;
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.app_red));

        magazineType = getIntent().getIntExtra(IntentConstants.TYPE, 0);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Magazine magazine = adapter.getItem(position);
                String postID = magazine.getPostId();
                if(postID!=null) {
                    Intent intent = new Intent(getBaseContext(), MagazineDetailsActivity.class);
                    intent.putExtra(IntentConstants.MAGAZINE_ID, postID);
                    startActivity(intent);
                }

            }
        });

        fetchIssueList();
    }

    @OnClick(R.id.back)
    public void onMBackClick() {
        finish();
    }

    @OnClick(R.id.calendarImg)
    public void onCalendaerClick() {
        MonthYearPicker myp = new MonthYearPicker(CategoryListingActivity.this);
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                textView1.setText(myp.getSelectedMonthName() + " >> " + myp.getSelectedYear());
            }
        }, null);
        myp.show();
    }

    private void fetchIssueList() {

        try {
            String filePath = root + File.separator + "Outlook/Magazines/issues-" + issueYear + ".json";
            Log.d(TAG,"Magazine Path::" + filePath);
            File file = new File(filePath);
            if (file.exists()) {
                gridView.setVisibility(View.VISIBLE);
                String response = Util.readJsonFromSDCard(filePath);
                System.out.println("Response::" + response);
                JsonReader reader = new JsonReader(new StringReader(response));
                reader.setLenient(true);

                Type listType = new TypeToken<ArrayList<IssuesVo>>() {}.getType();
                ArrayList<IssuesVo> issuesVo = new Gson().fromJson(reader, listType);

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                adapter = new OutlookGridViewAdapter(this, R.layout.grid_item_two_layout,getMonthList(issuesVo.get(0).getAcf()), width);
                SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
                AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
                animationAlphaAdapter.setAbsListView(gridView);
                gridView.setAdapter(animationAlphaAdapter);

            } else if (Util.isNetworkOnline(CategoryListingActivity.this)) {
                new DownloadFileFromURL(issueYear).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error Parsing content");
        }
    }

    private ArrayList<Magazine> getMonthList(Acf acf){

        ArrayList<Magazine> months = new ArrayList<Magazine>();

        ArrayList<WeeklyIssueVo> janMonthVo = (ArrayList<WeeklyIssueVo>) acf.getJanuary();
        for (int i=0;i<janMonthVo.size();i++){
            if(!janMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = janMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("JANUARY," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0)+"");
                months.add(magazine);
            }

        }
        ArrayList<WeeklyIssueVo> febMonthVo = (ArrayList<WeeklyIssueVo>) acf.getFebruary();
        for (int i=0;i<febMonthVo.size();i++){
            if(!febMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = febMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("FEBRUARY," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> marchMonthVo = (ArrayList<WeeklyIssueVo>) acf.getMarch();
        for (int i=0;i<marchMonthVo.size();i++){
            if(!marchMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = marchMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("MARCH," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> aprilMonthVo = (ArrayList<WeeklyIssueVo>) acf.getApril();
        for (int i=0;i<aprilMonthVo.size();i++){
            if(!aprilMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = aprilMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("APRIL," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> mayMonthVo = (ArrayList<WeeklyIssueVo>) acf.getMay();
        for (int i=0;i<mayMonthVo.size();i++){
            if(!mayMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = mayMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("MAY," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> juneMonthVo = (ArrayList<WeeklyIssueVo>) acf.getJune();
        for (int i=0;i<juneMonthVo.size();i++){
            if(!juneMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = juneMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("JUNE," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> julyMonthVo = (ArrayList<WeeklyIssueVo>) acf.getJuly();
        for (int i=0;i<julyMonthVo.size();i++){
            if(!julyMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = julyMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("JULY," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> augMonthVo = (ArrayList<WeeklyIssueVo>) acf.getAugust();
        for (int i=0;i<augMonthVo.size();i++){
            if(!augMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = augMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("AUGUST," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> sepMonthVo = (ArrayList<WeeklyIssueVo>) acf.getSeptember();
        for (int i=0;i<sepMonthVo.size();i++){
            if(!sepMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = sepMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("SEPTEMBER," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
            if(i == sepMonthVo.size()-1 && sepMonthVo.size() % 2 == 1){
                months.add(new Magazine());
            }
        }
        ArrayList<WeeklyIssueVo> octMonthVo = (ArrayList<WeeklyIssueVo>) acf.getOctober();
        for (int i=0;i<octMonthVo.size();i++){
            if(!octMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = octMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("OCTOBER," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
            if(i == octMonthVo.size()-1 && octMonthVo.size() % 2 == 1){
                months.add(new Magazine());
            }
        }
        ArrayList<WeeklyIssueVo> novMonthVo = (ArrayList<WeeklyIssueVo>) acf.getNovember();
        for (int i=0;i<novMonthVo.size();i++){
            if(!novMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = novMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("NOVEMBER," + issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0) + "");
                months.add(magazine);
            }
        }
        ArrayList<WeeklyIssueVo> decMonthVo = (ArrayList<WeeklyIssueVo>) acf.getDecember();
        for (int i=0;i<decMonthVo.size();i++){
            if(!decMonthVo.get(i).getDisplayName().equals("@nopost@")) {
                WeeklyIssueVo weeklyIssueVo = decMonthVo.get(i);


                Magazine magazine = new Magazine();
                magazine.setName(weeklyIssueVo.getDisplayName());
                magazine.setImage(weeklyIssueVo.getCoverImage());
                magazine.setIssueDate("DECEMBER,"+issueYear);
                magazine.setPostId(weeklyIssueVo.getSelectIssuePost().get(0)+"");
                months.add(magazine);
            }
        }



        return months;

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        String mPath;

        public DownloadFileFromURL(String fileName) {
            this.mPath = root + File.separator + "Outlook/Magazines/issues-" + issueYear + ".json";
            File file = new File(mPath);
            if (file.exists()) {
                file.delete();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast.show();
            File parentFolder = new File(root + File.separator + "Outlook");
            File subFolder = new File(root + File.separator + "Outlook/Magazines");
            if (!parentFolder.exists()) {
                parentFolder.mkdir();
            }
            if (!subFolder.exists()) {
                subFolder.mkdir();
            }
        }

        @Override
        protected String doInBackground(String... id) {
            int count;
            try {
                URL url = new URL(APIMethods.BASE_URL + APIMethods.ISSUE_LIST + issueYear);
                Log.d(TAG, "Download Json URL::" + url);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lenghtOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(mPath);
//                Log.d("GalleryFragment", "DOWNLOAD Output Path::" + output.toString());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                SessionManager.setDownloadFailed(CategoryListingActivity.this, true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "Downloaded JSON Successfully::");
            if (SessionManager.isDownloadFailed(CategoryListingActivity.this)) {
                stopDownload(mPath);
            }
            fetchIssueList();
            loadToast.success();
        }
    }

    private void stopDownload(String mFileName) {
        File imageFile = new File(mFileName);
        imageFile.delete();
        SessionManager.setDownloadFailed(CategoryListingActivity.this, false);
        loadToast.error();
        finish();
    }

}
