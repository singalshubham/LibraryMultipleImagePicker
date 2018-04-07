package com.shubhu.gallerylib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.shubhu.gallerylib.R;
import com.shubhu.gallerylib.adapter.GalleryImageAdapter;
import com.shubhu.gallerylib.constants.GalleryConstants;
import com.shubhu.gallerylib.model.GalleryModel;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private int conditionNumber;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //method for adding toolbar
        addingToolbar();
        final int initialNumberOfImages = getIntent().getIntExtra(GalleryConstants.NUMBER_OF_IMAGES, -1);
        final int initialSize = getIntent().getIntExtra(GalleryConstants.SIZE, -1);
        //setting the condition number according user requirement
        settingConditionNumber(initialNumberOfImages, initialSize);
        final GridView gridImage = (GridView) findViewById(R.id.grid_view_image);
        ArrayList<String> paths = getIntent().getStringArrayListExtra("imagePathList");
        final ArrayList<GalleryModel> pathList = new ArrayList<>();
        for (String path : paths) {
            GalleryModel galleryModel = new GalleryModel();
            galleryModel.setImagePath(path);
            galleryModel.setSelected(false);
            pathList.add(galleryModel);
        }


        final GalleryImageAdapter galleryImageAdapter
                = new GalleryImageAdapter(pathList,
                GalleryActivity.this);
        gridImage.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        gridImage.setAdapter(galleryImageAdapter);
        //setting the item click listener on grid item
        gridImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent slideImageActivity = new Intent(GalleryActivity.this, SlideImageActivity.class);
                slideImageActivity.putParcelableArrayListExtra("pathList", pathList);
                slideImageActivity.putExtra("position", position);
                GalleryActivity.this.startActivity(slideImageActivity);

            }
        });
        //setting the multiChoiceModeListener om gridImage
        gridImage.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int count = 0;
            int numberOfImages = 0;
            float size = 0;
            private Toast toast = Toast.makeText(GalleryActivity.this, getString(R.string.max_size_exceed),
                    Toast.LENGTH_SHORT);

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                switch (conditionNumber) {
                    //if there is no limitation on size and number of image
                    case 1:
                        if (checked) {
                            updateActionBarWhenChecked(mode, position);
                        } else {
                            updateActionBarWhenUnchecked(mode, position);
                        }
                        break;
                    //if there is limitation on both size and number of image
                    case 2:
                        if (checked) {
                            updateActionBarWhenChecked(mode, position);
                            size = size + calculateFileSize(pathList.get(position).getImagePath());
                            numberOfImages++;
                            if (size > initialSize || numberOfImages > initialNumberOfImages) {
                                if (!toast.getView().isShown()) {
                                    toast.show();
                                }
                                gridImage.setItemChecked(position, false);
                            }
                        } else {
                            updateActionBarWhenUnchecked(mode, position);
                            size = size - calculateFileSize(pathList.get(position).getImagePath());
                            numberOfImages--;
                        }
                        break;
                    //if there is limitation on only number of images
                    case 3:
                        if (checked) {
                            updateActionBarWhenChecked(mode, position);
                            numberOfImages++;
                            if (numberOfImages > initialNumberOfImages) {
                                if (!toast.getView().isShown()) {
                                    toast.show();
                                }
                                gridImage.setItemChecked(position, false);
                            }
                        } else {
                            updateActionBarWhenUnchecked(mode, position);
                            numberOfImages--;
                        }
                        break;
                    //if there is limitation on only size of images
                    case 4:
                        if (checked) {
                            updateActionBarWhenChecked(mode, position);
                            size = size + calculateFileSize(pathList.get(position).getImagePath());
                            if (size > initialSize) {
                                if (!toast.getView().isShown()) {
                                    toast.show();
                                }
                                gridImage.setItemChecked(position, false);
                            }
                        } else {
                            updateActionBarWhenUnchecked(mode, position);
                            size = size - calculateFileSize(pathList.get(position).getImagePath());
                        }
                        break;
                }
            }

            //update action bar when unCheck the item
            private void updateActionBarWhenUnchecked(ActionMode mode, int position) {
                count--;
                if (count == 1) {
                    mode.setTitle(count + " Item Selected");
                } else {
                    mode.setTitle(count + " Items Selected");
                }
                pathList.get(position).setSelected(false);
                galleryImageAdapter.notifyDataSetChanged();
            }

            //update action bar when check the item
            private void updateActionBarWhenChecked(ActionMode mode, int position) {
                count++;
                if (count == 1) {
                    mode.setTitle(count + " Item Selected");
                } else {
                    mode.setTitle(count + " Items Selected");
                }
                pathList.get(position).setSelected(true);
                galleryImageAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.menu_select) {
                    ArrayList<String> selectedPathList = new ArrayList<>();
                    for (GalleryModel galleryModel : pathList) {
                        if (galleryModel.isSelected()) {
                            selectedPathList.add(galleryModel.getImagePath());
                        }
                    }
                    mode.finish();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("selectedItemList", selectedPathList);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mode = null;
                count = 0;
                numberOfImages = 0;
                size = 0;
                for (GalleryModel galleryModel : pathList) {
                    galleryModel.setSelected(false);
                }
                galleryImageAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * method for setting the condition number according to user requirement
     */
    private void settingConditionNumber(int initialNumberOfImages, int initialSize) {
        //if there is no limitation on size and number of image
        if (initialNumberOfImages == -1 && initialSize == -1) {
            conditionNumber = 1;
        }
        //if there is limitation on both size and number of image
        else if (initialNumberOfImages != -1 && initialSize != -1) {
            conditionNumber = 2;
        }
        //if there is limitation on only number of images
        else if (initialNumberOfImages != -1) {
            conditionNumber = 3;
        }
        //if there is limitation on only size of images
        else {
            conditionNumber = 4;
        }
    }

    private void addingBackArrowOnToolbar() {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //calculating file size in MB
    private float calculateFileSize(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            // Get length of file in bytes
            long fileSizeInBytes = file.length();
            float fileSizeInKB = fileSizeInBytes / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            return fileSizeInKB / 1024;
        } else
            return (float) 0.0;
    }

    //method fod adding toolbar
    private void addingToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery_activity);
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
}
