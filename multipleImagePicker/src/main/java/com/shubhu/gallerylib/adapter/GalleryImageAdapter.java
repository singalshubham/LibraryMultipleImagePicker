package com.shubhu.gallerylib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shubhu.gallerylib.R;
import com.shubhu.gallerylib.activity.GalleryActivity;
import com.shubhu.gallerylib.model.GalleryModel;

import java.util.ArrayList;


public class GalleryImageAdapter extends BaseAdapter {
    private ArrayList<GalleryModel> imagePathList;
    private LayoutInflater layoutInflater;

    public GalleryImageAdapter(ArrayList<GalleryModel> imagePathList, GalleryActivity galleryActivity) {
        this.imagePathList = imagePathList;
        layoutInflater = LayoutInflater.from(galleryActivity);
    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_item_image, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_single_item);
            viewHolder.checkItem = (ImageView) convertView.findViewById(R.id.check_item);
            viewHolder.viewBackgroundCheck = convertView.findViewById(R.id.background_check_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (imagePathList.get(position).isSelected()) {
            viewHolder.checkItem.setVisibility(View.VISIBLE);
            viewHolder.viewBackgroundCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkItem.setVisibility(View.INVISIBLE);
            viewHolder.viewBackgroundCheck.setVisibility(View.INVISIBLE);
        }
        Glide.with(viewHolder.imageView.getContext())
                .load(imagePathList.get(position).getImagePath())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_place_holder)
                .into(viewHolder.imageView);
        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private ImageView checkItem;
        private View viewBackgroundCheck;
    }
}
