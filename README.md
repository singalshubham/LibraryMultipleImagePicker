### API MultipleImagePicker 
The api multipleImagePicker allows multiple image selection, we can limit the number of images and size of images according to requirement.
#### Impelementation steps

 - Copy folder in the project root directory.</br>
 for example </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Project_Name/MultipleImagePicker  
- Now go in the <span style="color:green">settings.gradle</span> file and replace 

```sh
include ':app'
```
with 

```sh
include ':app',':multipleImagePickerLib'
```

- Open app's <span style="color:green">build.gradle</span> file and add the <span style="color:blue">compile project <span style="color:green">(path:':logger')</span></span> line in depencies module such as : </br> 

```sh
dependencies {
    compile project(path: ':multipleImagePickerLib')

}
```
#### There are four possible multiple selections available for the user in this library.
-	No limit on size and number of images.
-	Limit on both size and number of images.
-	Limit on size of images only.
-	Limit on number of images only.

#### When user have limit on both size and number of images
 - if user require limitation on both then pass intent like following  

```sh
	     Intent galleryActivity = new Intent(MultipleImagePickerActivity.this, AlbumActivity.class);
        //size in MB
        galleryActivity.putExtra(GalleryConstants.SIZE,10);
        //pass max number of images can be selected
        galleryActivity.putExtra(GalleryConstants.NUMBER_OF_IMAGES, 10);
        startActivityForResult(galleryActivity, AlbumActivityRequest);
    
```
    
                
#### When user have no limit on size and number of images
 - if user don't require any limitation then pass intent like following  

```sh
	   Intent galleryActivity1 = new Intent(MultipleImagePickerActivity.this, AlbumActivity.class);
      startActivityForResult(galleryActivity1, AlbumActivityRequest);
      
```
#### When user have limit on number of images only
 - if user require limitation on number of images only then pass intent like following  

```sh
	    Intent galleryActivity2 = new Intent(MultipleImagePickerActivity.this, AlbumActivity.class);
	    galleryActivity2.putExtra(GalleryConstants.NUMBER_OF_IMAGES, 10);
        startActivityForResult(galleryActivity2, AlbumActivityRequest);
    
```
#### When user have limit on Size of images only
 - if user require limitation on size of images only then pass intent like following  

```sh
	     Intent galleryActivity3 = new Intent(MultipleImagePickerActivity.this, AlbumActivity.class);
         galleryActivity3.putExtra(GalleryConstants.SIZE,10);
         startActivityForResult(galleryActivity3, AlbumActivityRequest);
    
```

#### Overide the onActivityResult like following:
```sh
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumActivityRequest) {
            if (resultCode == RESULT_OK) {
                //it return the list of selected images
                Log.d("MultipleImagePicker", "selectred image path list is" +
                        data.getStringArrayListExtra(GalleryConstants.SELECTED_PATH_LIST));
                GridView gridView = (GridView) findViewById(R.id.gridView);
                GridAdapter gridAdapter = new GridAdapter(MultipleImagePickerActivity.this,
                data.getStringArrayListExtra(GalleryConstants.SELECTED_PATH_LIST));
                gridView.setAdapter(gridAdapter);
            }
        }
        
        ```
