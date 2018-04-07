package com.shubhu.gallerylib.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shubhu.gallerylib.R;
import com.shubhu.gallerylib.activity.SlideImageActivity;
import com.shubhu.gallerylib.model.GalleryModel;

import java.util.ArrayList;

public class SlideImageAdapter extends PagerAdapter {
    private final LayoutInflater layoutInflater;
    private ArrayList<GalleryModel> pathList;

    public SlideImageAdapter(SlideImageActivity slideImageActivity, ArrayList<GalleryModel> pathList) {
        layoutInflater = LayoutInflater.from(slideImageActivity);
        this.pathList = pathList;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView =
                layoutInflater.inflate(R.layout.layout_item_slide_image_pager, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_selected_zoom);

        loadImage(imageView, position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void loadImage(ImageView imageView, int position) {
        Glide.with(imageView.getContext())
                .load(pathList.get(position).getImagePath())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.ic_place_holder)
                .into(imageView);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
