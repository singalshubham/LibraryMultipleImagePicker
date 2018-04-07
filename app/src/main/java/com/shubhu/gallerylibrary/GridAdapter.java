package com.shubhu.gallerylibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class GridAdapter extends BaseAdapter {
    private ArrayList<String> selectedPathList;
    private LayoutInflater layoutInflater;

    public GridAdapter(SampleActivity sampleActivity, ArrayList<String> stringArrayListExtra) {
        this.selectedPathList = stringArrayListExtra;
        this.layoutInflater = LayoutInflater.from(sampleActivity);
    }

    @Override
    public int getCount() {
        return selectedPathList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.row_item, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_single);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(viewHolder.imageView.getContext())
                .load(selectedPathList.get(position))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.5f)
                .placeholder(com.shubhu.gallerylib.R.drawable.ic_place_holder)
                .into(viewHolder.imageView);
        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
    }
}
