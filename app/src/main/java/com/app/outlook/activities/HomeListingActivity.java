package com.app.outlook.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.outlook.R;
import com.app.outlook.Utils.APIMethods;
import com.app.outlook.Utils.Util;
import com.app.outlook.fragments.HomeListFragment;
import com.app.outlook.fragments.HomeGridFragment;
import com.app.outlook.listener.OnThemeChangeListener;
import com.app.outlook.manager.SessionManager;
import com.app.outlook.manager.SharedPrefManager;
import com.app.outlook.modal.FeedParams;
import com.app.outlook.modal.IntentConstants;
import com.app.outlook.modal.MagazineTypeVo;
import com.app.outlook.modal.OutlookConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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

public class HomeListingActivity extends AppBaseActivity implements OnThemeChangeListener{

    private static final String TAG = "HomeListingActivity";
    private ArrayList<MagazineTypeVo> magazineList;
    private HomeGridFragment magazineGridFragment;
    private HomeListFragment magazineListFragment;
    private LoadToast loadToast;

    public static int PAGES = 2;
    public final static int LOOPS = 1;
    public final static int FIRST_PAGE = 0;//PAGES * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    @Bind(R.id.carouselView)
    ImageButton carouselView;
    @Bind(R.id.gridView)
    ImageButton gridView;
    @Bind(R.id.toolbar_title)
    ImageView titleImg;
    private DownloadFileFromURL task;
    private String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_exit);
        setContentView(R.layout.activity_home_listing);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        root =  getCacheDir().getAbsolutePath();
        //Environment.getExternalStorageDirectory().getAbsoluteFile().toString();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        loadToast = new LoadToast(this);
        loadToast.setText("Loading...");
        int height = size.y;
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.app_red));

        carouselView.setSelected(true);
        fetchMagazineList();
    }

    private void fetchMagazineList() {

        try {
            String filePath = root + File.separator + "Outlook/Magazines/list.json";
            Log.d(TAG,"Magazine Path::" + filePath);
            File file = new File(filePath);
            if (!Util.isNetworkOnline(HomeListingActivity.this) && file.exists()) {
                loadFragments(filePath);

            } else if (Util.isNetworkOnline(HomeListingActivity.this)) {
                task = new DownloadFileFromURL();
                task.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error Parsing content");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        prefManager.init(this);
        prefManager.setSharedData(OutlookConstants.theme, R.style.AppTheme);
        setTheme(R.style.AppTheme);
        if(task != null && task.isCancelled() ){
            if(magazineList == null || magazineList.isEmpty()){
                fetchMagazineList();
            }
        }
    }

    private void loadFragments(String filePath) {

        String response = Util.readJsonFromSDCard(filePath);
        System.out.println("Response::" + response);
        JsonReader reader = new JsonReader(new StringReader(response));
        reader.setLenient(true);

        Type listType = new TypeToken<ArrayList<MagazineTypeVo>>() {}.getType();
        magazineList = new Gson().fromJson(reader, listType);
        if(magazineList != null && !magazineList.isEmpty()) {
            PAGES = magazineList.size();

            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentConstants.MAGAZINE_LIST, magazineList);

            magazineGridFragment = new HomeGridFragment();
            magazineGridFragment.setArguments(bundle);

            magazineListFragment = new HomeListFragment();
            magazineListFragment.setArguments(bundle);
magazineListFragment.setOnThemeChangeListener(this);
            changeFragment(magazineListFragment);
        }
    }

    @OnClick(R.id.gridView)
    public void onGridviewClick() {
        changeFragment(magazineGridFragment);

        carouselView.setSelected(false);
        gridView.setSelected(true);
    }

    @OnClick(R.id.imgSettings)
    public void onSettingsClick(){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @OnClick(R.id.carouselView)
    public void onCarouselViewClick() {
        changeFragment(magazineListFragment);

        carouselView.setSelected(true);
        gridView.setSelected(false);
    }

    private void changeFragment(Fragment fragment) {
        if(!magazineList.isEmpty()) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.contentPanel, fragment)
                    .commit();
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        String mPath;

        public DownloadFileFromURL() {
            this.mPath = root + File.separator + "Outlook/Magazines/list.json";
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
                URL url = new URL(APIMethods.BASE_URL+APIMethods.MAGAZINE_TYPE_LIST+"?"+
                        FeedParams.USER_ID+"="+ SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID)
                        + "&"+ FeedParams.TOKEN+"="+SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN));
                        Log.d(TAG, "Download Json URL::" + url);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lenghtOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(mPath);

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
                SessionManager.setDownloadFailed(HomeListingActivity.this, true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "Downloaded JSON Successfully::");
            if (SessionManager.isDownloadFailed(HomeListingActivity.this)) {
                stopDownload(mPath);
            }
            loadFragments(mPath);
            loadToast.success();
        }

    }

    private void stopDownload(String mFileName) {
        File file = new File(mFileName);
        file.delete();
        SessionManager.setDownloadFailed(HomeListingActivity.this, false);
        loadToast.error();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(task != null ) {
            task.cancel(true);
            loadToast.error();
        }
    }

    @Override
    public void onMagazineTheme(int position) {
       switch (position){
           case 0:
               titleImg.setImageResource(R.drawable.icon_outlook_group);
               break;
           case 1:
               titleImg.setImageResource(R.drawable.icon_outlook);
               break;
           case 2:
               titleImg.setImageResource(R.drawable.logo_outlook);
               break;
           case 3:
               titleImg.setImageResource(R.drawable.icon_outlook);
               break;
           case 4:
               titleImg.setImageResource(R.drawable.logo_outlook);
               break;
           case 5:
               titleImg.setImageResource(R.drawable.icon_outlook);
               break;
           default:
               titleImg.setImageResource(R.drawable.icon_outlook_group);
               break;
       }
    }
}
