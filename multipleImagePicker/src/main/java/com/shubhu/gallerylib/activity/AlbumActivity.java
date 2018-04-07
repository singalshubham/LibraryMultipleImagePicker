package com.shubhu.gallerylib.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shubhu.gallerylib.R;
import com.shubhu.gallerylib.adapter.AlbumAdapter;
import com.shubhu.gallerylib.constants.GalleryConstants;
import com.shubhu.gallerylib.filter.ImageFilter;
import com.shubhu.gallerylib.model.AlbumModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        //number of images user want to select
        final int numberOfImages = getIntent().getIntExtra(GalleryConstants.NUMBER_OF_IMAGES, -1);
        //size user want to select
        final int size = getIntent().getIntExtra(GalleryConstants.SIZE, -1);
        //adding toolbar
        addingToolbar();
        if (ActivityCompat.checkSelfPermission(AlbumActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AlbumActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GalleryConstants.WRITE_EXTERNAL_STORAGE_PERMISSION);
        } else {
            //querying data and setting adapter for gridView
            settingAdapterForGridView(size, numberOfImages);
        }
    }

    /**
     * method for query of folder and setting the adapter for gridview
     *
     * @param size           limit that user want
     * @param numberOfImages limit according user
     */
    private void settingAdapterForGridView(final int size, final int numberOfImages) {
        //view for grid of folder
        GridView gridView = (GridView) findViewById(R.id.grid_image_album);
        final ArrayList<AlbumModel> listAlbum = new ArrayList<>();
        // which image properties are we querying
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA};
        // We want to order the albums by reverse chronological order. We abuse the
        // "WHERE" parameter to insert a "GROUP BY" clause into the SQL statement.
        // The template for "WHERE" parameter is like:
        //    SELECT ... FROM ... WHERE (%s)
        // and we make it look like:
        //    SELECT ... FROM ... WHERE (1) GROUP BY 1,(2)
        // The "(1)" means true. The "1,(2)" means the first two columns specified
        // after SELECT. Note that because there is a ")" in the template, we use
        // "(2" to match it.
        String BUCKET_GROUP_BY =
                "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        // Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = getContentResolver().query(
                images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

        if (null != cur && cur.moveToFirst()) {
            String bucketName;
            String dateTaken;
            String folderImagePath;
            int idOfFolder;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            int idColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

            do {
                // Get the field values
                bucketName = cur.getString(bucketColumn);
                dateTaken = cur.getString(dateColumn);
                folderImagePath = cur.getString(dataColumn);
                idOfFolder = cur.getInt(idColumn);

                listAlbum.add(new AlbumModel(bucketName, folderImagePath,
                        noOfFilesInFolder(folderImagePath), idOfFolder));
            } while (cur.moveToNext());
            cur.close();
        }
        AlbumAdapter albumAdapter = new AlbumAdapter(AlbumActivity.this, listAlbum);
        gridView.setAdapter(albumAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent galleryActivity = new Intent(AlbumActivity.this, GalleryActivity.class);
                galleryActivity.putExtra("imagePathList",
                        listOfFilesInFolder(listAlbum.get(position).getFolderImage()));
                galleryActivity.putExtra(GalleryConstants.SIZE, size);
                galleryActivity.putExtra(GalleryConstants.NUMBER_OF_IMAGES, numberOfImages);
                startActivityForResult(galleryActivity, GalleryConstants.GALLERY_ACTIVITY_REQUEST);
            }
        });
    }

    //method fod adding toolbar
    private void addingToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sample_activity);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar())
            getSupportActionBar().setTitle(getString(R.string.toolbar_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //adding back arrow on toolbar
        addingBackArrowOnToolbar();
    }

    //list of path in folder
    public ArrayList<String> listOfFilesInFolder(String folderImagePath) {
        File folderImageFile = new File(folderImagePath);
        File folderPath = new File(folderImageFile.getParent());
        ArrayList<String> imagePathList = new ArrayList<>();
        List<File> fileList = Arrays.asList(folderPath.listFiles(new ImageFilter()));
        Collections.reverse(fileList);
        for (File file : fileList) {
            imagePathList.add(file.getPath());
        }

        return imagePathList;
    }

    //count no of files in folder
    private int noOfFilesInFolder(String folderImagePath) {
        File folderImageFile = new File(folderImagePath);
        File folderPath = new File(folderImageFile.getParent());
        File listFiles[] = folderPath.listFiles(new ImageFilter());
        return listFiles.length;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryConstants.GALLERY_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(GalleryConstants.SELECTED_PATH_LIST,
                        data.getStringArrayListExtra("selectedItemList"));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    //adding backArrowOnToolbar
    private void addingBackArrowOnToolbar() {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GalleryConstants.WRITE_EXTERNAL_STORAGE_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    settingAdapterForGridView(getIntent().getIntExtra(GalleryConstants.SIZE, -1),
                            getIntent().getIntExtra(GalleryConstants.NUMBER_OF_IMAGES, -1));
                } else {
                    //finishing the activity when no permission is granted
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
