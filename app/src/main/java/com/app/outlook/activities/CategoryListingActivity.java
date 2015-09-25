package com.app.outlook.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.outlook.R;
import com.app.outlook.adapters.OutlookGridViewAdapter;
import com.app.outlook.model.IntentConstants;
import com.app.outlook.model.Magazine;
import com.app.outlook.views.MonthYearPicker;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by srajendrakumar on 10/09/15.
 */
public class CategoryListingActivity extends AppBaseActivity{

    private ArrayList<Magazine> magazineList;
    private OutlookGridViewAdapter adapter;
    @Bind(R.id.gridView)
    GridView gridView;
    private int magazineType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_listing);
        ButterKnife.bind(this);
        loadDummyDataOutlook();
        initView();
    }

    private void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        magazineType = getIntent().getIntExtra(IntentConstants.TYPE,0);
        adapter = new OutlookGridViewAdapter(this,R.layout.grid_item_two_layout,magazineList,width);
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(adapter);
        AlphaInAnimationAdapter animationAlphaAdapter = new AlphaInAnimationAdapter(animationAdapter);
        animationAlphaAdapter.setAbsListView(gridView);
        gridView.setAdapter(animationAlphaAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(getBaseContext(), MagazineDetailsActivity.class));
            }
        });
    }

    @OnClick(R.id.back)
    public void onMBackClick(){
        finish();
    }

    @OnClick(R.id.calendarImg)
    public void onCalendaerClick(){
        MonthYearPicker myp = new MonthYearPicker(CategoryListingActivity.this);
        myp.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                textView1.setText(myp.getSelectedMonthName() + " >> " + myp.getSelectedYear());
            }
        }, null);
        myp.show();
    }

    private void loadDummyData() {

        magazineList = new ArrayList<>();
        Magazine m1 = new Magazine();
        m1.setName("OUTLOOK");
        m1.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m1.setId(1);
        m1.setImage(R.drawable.outlook_travel_dummy1);
        m1.setIssueDate("JUNE, 2015");
        magazineList.add(m1);

        Magazine m2 = new Magazine();
        m2.setName("OUTLOOK TRAVELLER");
        m2.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m2.setId(2);
        m2.setImage(R.drawable.outlook_traveller_dummy2);
        m2.setIssueDate("JULY, 2015");
        magazineList.add(m2);

        Magazine m3 = new Magazine();
        m3.setName("OUTLOOK MONEY");
        m3.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m3.setId(3);
        m3.setImage(R.drawable.outlook_traveller_dummy3);
        m3.setIssueDate("AUGUST, 2015");
        magazineList.add(m3);

        Magazine m4 = new Magazine();
        m4.setName("OUTLOOK HINDI");
        m4.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m4.setId(4);
        m4.setImage(R.drawable.outlook_traveller_dummy4);
        m4.setIssueDate("SPETEMBER, 2015");
        magazineList.add(m4);

        Magazine m5 = new Magazine();
        m5.setName("OUTLOOK BUSINESS");
        m5.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m5.setId(5);
        m5.setImage(R.drawable.outlook_traveller_dummy5);
        m5.setIssueDate("OCTOBER, 2015");
        magazineList.add(m5);

    }

    private void loadDummyDataOutlook() {

        magazineList = new ArrayList<>();
        Magazine m1 = new Magazine();
        m1.setName("OUTLOOK");
        m1.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m1.setId(1);
        m1.setImage(R.drawable.outlook_coverpage);
        m1.setIssueDate("JUNE, 2015");
        magazineList.add(m1);

        Magazine m2 = new Magazine();
        m2.setName("OUTLOOK TRAVELLER");
        m2.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m2.setId(2);
        m2.setImage(R.drawable.outlook_coverpage);
        m2.setIssueDate("JUNE, 2015");
        magazineList.add(m2);

        Magazine m3 = new Magazine();
        m3.setName("OUTLOOK MONEY");
        m3.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m3.setId(3);
        m3.setImage(R.drawable.outlook_coverpage);
        m3.setIssueDate("JUNE, 2015");
        magazineList.add(m3);

        Magazine m4 = new Magazine();
        m4.setName("OUTLOOK HINDI");
        m4.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m4.setId(4);
        m4.setImage(R.drawable.outlook_coverpage);
        m4.setIssueDate("JUNE, 2015");
        magazineList.add(m4);

        Magazine m5 = new Magazine();
        m5.setName("OUTLOOK BUSINESS");
        m5.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m5.setId(5);
        m5.setImage(R.drawable.outlook_coverpage);
        m5.setIssueDate("July, 2015");
        magazineList.add(m5);

        Magazine m6 = new Magazine();
        m6.setName("OUTLOOK BUSINESS");
        m6.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m6.setId(6);
        m6.setImage(R.drawable.outlook_coverpage);
        m6.setIssueDate("July, 2015");
        magazineList.add(m6);

        Magazine m7 = new Magazine();
        m7.setName("OUTLOOK BUSINESS");
        m7.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m7.setId(7);
        m7.setImage(R.drawable.outlook_coverpage);
        m7.setIssueDate("July, 2015");
        magazineList.add(m7);

        Magazine m8 = new Magazine();
        m8.setName("OUTLOOK BUSINESS");
        m8.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m8.setId(8);
        m8.setImage(R.drawable.outlook_coverpage);
        m8.setIssueDate("July, 2015");
        magazineList.add(m8);

        Magazine m9 = new Magazine();
        m9.setName("OUTLOOK BUSINESS");
        m9.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m9.setId(9);
        m9.setImage(R.drawable.outlook_coverpage);
        m9.setIssueDate("August, 2015");
        magazineList.add(m9);

        Magazine m10 = new Magazine();
        m10.setName("OUTLOOK BUSINESS");
        m10.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m10.setId(10);
        m10.setImage(R.drawable.outlook_coverpage);
        m10.setIssueDate("August, 2015");
        magazineList.add(m10);

        Magazine m11 = new Magazine();
        m11.setName("OUTLOOK BUSINESS");
        m11.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m11.setId(11);
        m11.setImage(R.drawable.outlook_coverpage);
        m11.setIssueDate("August, 2015");
        magazineList.add(m11);

        Magazine m12 = new Magazine();
        m12.setName("OUTLOOK BUSINESS");
        m12.setDesciption("Lorem ipsum dolor sit er elit lamet, consectetaur cillium adipisicing pecu, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut");
        m12.setId(12);
        m12.setImage(R.drawable.outlook_coverpage);
        m12.setIssueDate("August, 2015");
        magazineList.add(m12);

    }


}
