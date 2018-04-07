package com.shubhu.gallerylib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shubhu.gallerylib.R;
import com.shubhu.gallerylib.activity.AlbumActivity;
import com.shubhu.gallerylib.model.AlbumModel;

import java.util.ArrayList;
import java.util.Locale;

public class AlbumAdapter extends BaseAdapter {
    private ArrayList<AlbumModel> listAlbum;
    private LayoutInflater layoutInflater;

    public AlbumAdapter(AlbumActivity sampleActivity, ArrayList<AlbumModel> listAlbum) {
        this.listAlbum = listAlbum;
        this.layoutInflater = LayoutInflater.from(sampleActivity);
    }

    @Override
    public int getCount() {
        return listAlbum.size();
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
        ViewHolder gridItemHolder = new ViewHolder();
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_item_album, parent, false);
            gridItemHolder.imageAlbum = (ImageView) convertView.findViewById(R.id.image_album);
            gridItemHolder.textFolderName = (TextView) convertView.findViewById(R.id.text_album_name);
            gridItemHolder.textImageCount = (TextView) convertView.findViewById(R.id.text_no_of_image);
            convertView.setTag(gridItemHolder);
        } else {
            gridItemHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(gridItemHolder.imageAlbum.getContext())
                .load(listAlbum.get(position).getFolderImage())
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_place_holder)
                .into(gridItemHolder.imageAlbum);
        gridItemHolder.textFolderName.setText(listAlbum.get(position).getFolderName());
        gridItemHolder.textImageCount.setText(String.format(Locale.ENGLISH,
                "%d", listAlbum.get(position).getImageCount()));

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageAlbum;
        private TextView textFolderName;
        private TextView textImageCount;
    }

}
