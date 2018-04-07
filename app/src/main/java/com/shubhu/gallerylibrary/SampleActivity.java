package com.shubhu.gallerylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.shubhu.gallerylib.activity.AlbumActivity;
import com.shubhu.gallerylib.constants.GalleryConstants;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {
    private final int AlbumActivityRequest = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        findViewById(R.id.btn_condition_both).setOnClickListener(this);
        findViewById(R.id.btn_condition_nothing).setOnClickListener(this);
        findViewById(R.id.btn_condition_size).setOnClickListener(this);
        findViewById(R.id.btn_condition_number).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumActivityRequest) {
            if (resultCode == RESULT_OK) {
                Log.d("SampleActivity", "selectred image path list is" +
                        data.getStringArrayListExtra(GalleryConstants.SELECTED_PATH_LIST));
                GridView gridView = (GridView) findViewById(R.id.gridView);
                GridAdapter gridAdapter = new GridAdapter(SampleActivity.this,
                        data.getStringArrayListExtra(GalleryConstants.SELECTED_PATH_LIST));
                gridView.setAdapter(gridAdapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_condition_both:
                Intent galleryActivity = new Intent(SampleActivity.this, AlbumActivity.class);
                //size in MB
                galleryActivity.putExtra(GalleryConstants.SIZE, 20);
                galleryActivity.putExtra(GalleryConstants.NUMBER_OF_IMAGES, 10);
                startActivityForResult(galleryActivity, AlbumActivityRequest);
                break;
            case R.id.btn_condition_nothing:
                Intent galleryActivity1 = new Intent(SampleActivity.this, AlbumActivity.class);
                startActivityForResult(galleryActivity1, AlbumActivityRequest);
                break;
            case R.id.btn_condition_number:
                Intent galleryActivity2 = new Intent(SampleActivity.this, AlbumActivity.class);
                galleryActivity2.putExtra(GalleryConstants.NUMBER_OF_IMAGES, 10);
                startActivityForResult(galleryActivity2, AlbumActivityRequest);
                break;
            case R.id.btn_condition_size:
                Intent galleryActivity3 = new Intent(SampleActivity.this, AlbumActivity.class);
                galleryActivity3.putExtra(GalleryConstants.SIZE, 20);
                startActivityForResult(galleryActivity3, AlbumActivityRequest);
                break;
        }
    }
}
