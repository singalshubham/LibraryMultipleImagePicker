package com.shubhu.gallerylib.filter;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by shubhu on 17/4/17.
 */

public class ImageFilter implements FilenameFilter {

    private  String[] imagesExtensions ;

    public ImageFilter() {
        imagesExtensions=new String[] { "jpg", "png", "jpe", "jpeg", "bmp", "webp" };
    }

    @Override
    public boolean accept(File dir, String filename) {
        if (new File(dir, filename).isFile()) {
            for (String extension : imagesExtensions)
                if (filename.toLowerCase().endsWith(extension))
                    return true;
        }

        return false;
    }}
