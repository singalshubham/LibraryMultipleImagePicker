package com.shubhu.gallerylib.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shubhu.gallerylib.R;
import com.shubhu.gallerylib.adapter.SlideImageAdapter;
import com.shubhu.gallerylib.model.GalleryModel;

import java.util.ArrayList;

public class SlideImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_image);
        //method for adding toolbar
        addingToolbar();
        int position = getIntent().getIntExtra("position", 0);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_slide_image);
        ArrayList<GalleryModel> pathList = getIntent().getParcelableArrayListExtra("pathList");
        SlideImageAdapter slideImageAdapter = new SlideImageAdapter(SlideImageActivity.this, pathList);
        viewPager.setAdapter(slideImageAdapter);
        viewPager.setCurrentItem(position);
    }

    //method for adding toolbar
    private void addingToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.slide_image_activity_toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar())
            getSupportActionBar().setTitle(R.string.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addingBackArrowOnToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addingBackArrowOnToolbar() {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}
