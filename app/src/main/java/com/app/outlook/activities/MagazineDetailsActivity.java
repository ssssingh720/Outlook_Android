package com.app.outlook.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.app.outlook.R;
import com.app.outlook.fragments.MagazineDetailsFragment;
import com.app.outlook.fragments.SectionDetailsHolderFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 21/09/15.
 */
public class MagazineDetailsActivity extends AppBaseActivity{

    public static final String EXTRA_NAME = "cheese_name";

    SectionDetailsHolderFragment sectionDetailsHolderFragment;
    MagazineDetailsFragment magazineDetailsFragment;
    private String TAG = "MagazineDetailsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        if(sectionDetailsHolderFragment.isRemoving()){
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.show(magazineDetailsFragment);
                        }
                    }
                });

        magazineDetailsFragment = new MagazineDetailsFragment();
        changeFragment(magazineDetailsFragment,false);
    }

    @OnClick(R.id.back)
    public void onMBackClick(){
        onBackPressed();
    }

    private void changeFragment(Fragment fragment, boolean backStack){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
        transaction.add(R.id.contentPanel, fragment);
        if(backStack) {
            transaction.hide(magazineDetailsFragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void openSectionDetails(int currnetPage){
        sectionDetailsHolderFragment = new SectionDetailsHolderFragment();
        changeFragment(sectionDetailsHolderFragment, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_listing, menu);
        return true;
    }

    //Params:
    // jsonResponse: pass directly new Gson().fromJson here.
    //path: Folder path to store json
    public void saveJsonToSDCard(String jsonResponse, String path){
        try {


            File file = new File(path);
            Log.d("BRAND", "write to sd card" + file);
            if (file.exists())
                file.delete();

            file.createNewFile();
            file.setReadable(true);
            file.setExecutable(true);
            file.setWritable(true);

            Log.d("BRAND", "write to sd card");
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(jsonResponse);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            Log.d("BRAND", "Exception" + e.toString());
        }
    }

    //Params:
    // path: Path  to the folder without "/"
    //fileName: Name of the File
    public String readJsonFromSDCard(String path, String fileName) {

        try {

            File jsonPath = new File(path + File.separator + fileName);

            if (jsonPath.exists()) {
                FileInputStream fIn = new FileInputStream(jsonPath);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));

                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";

                }
                Log.d("BRAND", "Buffer::" + aBuffer);

                return aBuffer.toString();

            } else {
                Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    //Params:
    // thumbnailsPath: Path  to the folder without "/"
    //thumbnailsImage: Name of the image
    private void downloadImagesToSdCard(final File thumbnailsPath, final String thumbnailImage) {


        Target mTarget = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                // Perform simple file operation to store this bitmap to your sd card
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        File file = new File(thumbnailsPath + "/" + thumbnailImage.replace("model_thumbnails/", ""));
                        Log.d(TAG, "File::" + file);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(TAG, "onBitmapFailed::" + errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "onPrepareLoad::");
            }
        };

        Picasso.with(getApplicationContext()).load(Uri.parse("MEDIA_URL" + thumbnailImage)).into(mTarget);
    }
}
