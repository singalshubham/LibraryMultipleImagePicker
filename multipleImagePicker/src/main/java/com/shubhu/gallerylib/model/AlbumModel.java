package com.shubhu.gallerylib.model;

/**
 * @author Ranosys Technologies
 */

public class AlbumModel {
    private String folderName;
    private String folderImage;
    private int imageCount;
    private int folderId;

    public AlbumModel(String folderName, String folderImage, int imageCount, int folderId) {
        this.folderName = folderName;
        this.folderImage = folderImage;
        this.imageCount = imageCount;
        this.folderId = folderId;
    }

    public String getFolderName() {

        return folderName;
    }

    public String getFolderImage() {
        return folderImage;
    }

    public int getImageCount() {
        return imageCount;
    }

    public int getFolderId() {
        return folderId;
    }

}
